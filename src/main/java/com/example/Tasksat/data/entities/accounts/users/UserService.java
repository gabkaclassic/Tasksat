package com.example.Tasksat.data.entities.accounts.users;

import com.example.Tasksat.data.entities.accounts.Account;
import com.example.Tasksat.handling.responses.AuthorizationResponse;
import com.example.Tasksat.handling.responses.RegistrationResponse;
import com.example.Tasksat.handling.utils.JWTUtil;
import com.example.Tasksat.handling.utils.MailUtil;
import com.example.Tasksat.handling.utils.validators.AccountValidator;
import com.example.Tasksat.handling.utils.validators.data.UserData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    private final JWTUtil jwtUtil;

    private final PasswordEncoder encoder;

    private final MailUtil mailUtil;

    @Autowired
    @Qualifier("userValidator")
    private final AccountValidator validator;

    private static final Random random = new Random();

    private static final ResponseEntity<AuthorizationResponse> UNAUTHORIZED = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);


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
                        validator.validate(new UserData(login, password, email), violations);

                    if(!violations.isEmpty())
                        return ResponseEntity.ok().body(new RegistrationResponse(violations));

                    var account = new User();
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
                .doOnNext(User::unlock)
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

    private void save(User account) {
        repository.save(account).subscribe();
    }

    public Flux<User> all() {
        return repository.findAll();
    }
}