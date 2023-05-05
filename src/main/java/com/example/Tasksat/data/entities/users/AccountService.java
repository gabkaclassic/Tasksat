package com.example.Tasksat.data.entities.users;

import com.example.Tasksat.handling.responses.AuthorizationResponse;
import com.example.Tasksat.handling.responses.RegistrationResponse;
import com.example.Tasksat.handling.utils.AccountDataValidatorUtil;
import com.example.Tasksat.handling.utils.JWTUtil;
import com.example.Tasksat.handling.utils.MailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccountService implements ReactiveUserDetailsService {

    private final AccountRepository repository;

    private final JWTUtil jwtUtil;

    private final PasswordEncoder encoder;

    private final MailUtil mailUtil;


    private final AccountDataValidatorUtil validator;

    private static final Random random = new Random();

    private static final ResponseEntity<AuthorizationResponse> UNAUTHORIZED = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);


    @Override
    public Mono<UserDetails> findByUsername(String login) {
        return repository.findByLogin(login);
    }

    public Mono<ResponseEntity<RegistrationResponse>> registry(String login, String password, String email)  {


        return repository
                .existsByLoginOrEmail(login, email)
                .map(exists -> {

                    var violations = new ArrayList<String>();

                    if(exists)
                        violations.add("Account with this login/email already exists");
                    else
                        validator.validate(login, password, email, violations);

                    if(!violations.isEmpty())
                        return ResponseEntity.ok().body(new RegistrationResponse(violations));

                    var account = new Account();
                    account.setLogin(login);
                    account.setPassword(encoder.encode(password));
                    account.setEmail(email);
                    account.setCode(randomConfirmationCode());
                    repository.save(account).subscribe();

                    try {
                        mailUtil.addToMessageQueue(account);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    return ResponseEntity.ok(new RegistrationResponse(violations));
                });

    }

    public Mono<ResponseEntity<AuthorizationResponse>> login(String login, String password) {


        return findByUsername(login).cast(Account.class)
                .map(
                        account -> {

                            if(account == null || !encoder.matches(password, account.getPassword()) && account.isAccountNonLocked())
                                return UNAUTHORIZED;

                            String token = jwtUtil.generateToken(account);
                            return ResponseEntity.ok().body(new AuthorizationResponse(token));
                        }
                );
    }

    public void confirm(String code) {


        repository.findByCode(code)
                .filter(Objects::nonNull)
                .doOnNext(account ->  account.setCode(null))
                .log()
                .subscribe(account -> repository.save(account).subscribe());

    }

    private static String randomConfirmationCode() {

        return random.ints(48, 123)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(8)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    private void save(Account account) {
        repository.save(account).subscribe();
    }

    public Flux<Account> all() {
        return repository.findAll();
    }

    public Mono<ResponseEntity<List<String>>> update(
            String oldLogin,
            String newLogin,
            String oldPassword,
            String newPassword,
            String newEmail) {

        return repository
                .findByLogin(oldLogin).cast(Account.class).defaultIfEmpty(null)
                .map(account -> {

                    var violations = new ArrayList<String>();

                    if(newLogin == null && newPassword == null && newEmail == null)
                        violations.add("Nothing to update");

                    if(account == null)
                        violations.add("Account with this login not exists");
                    else if(!encoder.matches(oldPassword, account.getPassword()))
                        violations.add("Wrong password");
                    else {

                        if(newLogin != null)
                            validator.validLogin(newLogin, violations);
                        if(newPassword != null)
                            validator.validPassword(newPassword, violations);
                        if(newEmail != null)
                            validator.validEmail(newEmail, violations);
                    }


                    if(!violations.isEmpty())
                        return ResponseEntity.badRequest().body(violations);

                    if(newLogin != null)
                        account.setLogin(newLogin);
                    if(newPassword != null)
                        account.setPassword(newPassword);
                    if(newEmail != null)
                        account.setEmail(newEmail);

                    account.setCode(randomConfirmationCode());
                    save(account);

                    try {

                        mailUtil.addToMessageQueue(account);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    return ResponseEntity.ok(violations);
                });

    }
}