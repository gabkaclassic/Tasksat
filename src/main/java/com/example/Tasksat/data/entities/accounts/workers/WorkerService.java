package com.example.Tasksat.data.entities.accounts.workers;

import com.example.Tasksat.data.entities.accounts.Account;
import com.example.Tasksat.data.entities.accounts.users.User;
import com.example.Tasksat.handling.responses.AuthorizationResponse;
import com.example.Tasksat.handling.responses.RegistrationResponse;
import com.example.Tasksat.handling.utils.validators.AccountDataValidatorUtil;
import com.example.Tasksat.handling.utils.JWTUtil;
import com.example.Tasksat.handling.utils.validators.AccountValidator;
import com.example.Tasksat.handling.utils.validators.data.AccountData;
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
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class WorkerService {

    private final WorkerRepository repository;

    private final JWTUtil jwtUtil;

    private final PasswordEncoder encoder;

    @Autowired
    @Qualifier("userValidator")
    private final AccountValidator validator;
    private static final ResponseEntity<AuthorizationResponse> UNAUTHORIZED = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

    public Mono<UserDetails> findByUsername(String login) {
        return repository.findByLogin(login);
    }

    public Mono<ResponseEntity<RegistrationResponse>> registry(String login, String password)  {


        return repository
                .existsByLogin(login)
                .map(exists -> {

                    var violations = new ArrayList<String>();

                    if(exists)
                        violations.add("Account with this login/email already exists");
                    else
                        validator.validate(new AccountData(login, password), violations);

                    if(!violations.isEmpty())
                        return ResponseEntity.ok().body(new RegistrationResponse(violations));

                    var account = new Worker();
                    account.setLogin(login);
                    account.setPassword(encoder.encode(password));
                    repository.save(account).subscribe();

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
    private void save(Worker account) {
        repository.save(account).subscribe();
    }

    public Flux<Worker> all() {
        return repository.findAll();
    }

}