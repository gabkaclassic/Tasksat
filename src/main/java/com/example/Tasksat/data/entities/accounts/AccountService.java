package com.example.Tasksat.data.entities.accounts;

import com.example.Tasksat.data.entities.accounts.admins.AdminService;
import com.example.Tasksat.data.entities.accounts.users.UserService;
import com.example.Tasksat.data.entities.accounts.workers.Worker;
import com.example.Tasksat.data.entities.accounts.workers.WorkerService;
import com.example.Tasksat.handling.requests.AuthorizationRequest;
import com.example.Tasksat.handling.requests.RegistrationRequest;
import com.example.Tasksat.handling.responses.AuthorizationResponse;
import com.example.Tasksat.handling.responses.RegistrationResponse;
import com.example.Tasksat.handling.utils.JWTUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AccountService {

    @Getter
    private final UserService userService;

    @Getter
    private final WorkerService workerService;

    @Getter
    private final AdminService adminService;

    private static final Mono<ResponseEntity<AuthorizationResponse>> UNAUTHORIZED = Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null));

    public Mono<ResponseEntity<AuthorizationResponse>> login(AuthorizationRequest data) {

        return switch (data.iam()) {
            case "USER" -> userService.login(data.login(), data.password());
            case "WORKER" -> workerService.login(data.login(), data.password());
            case "ADMIN" -> adminService.login(data.login(), data.password());
            default -> UNAUTHORIZED;
        };
    }

    public Mono<ResponseEntity<RegistrationResponse>> registration(RegistrationRequest data) {

        return switch (data.iam()) {
            case "USER" -> {
                if(data.email() == null)
                    yield Mono.just(ResponseEntity.ok().body(new RegistrationResponse(List.of("Email is required"))));

                yield userService.registry(data.login(), data.password(), data.email());
            }
            case "WORKER" -> workerService.registry(data.login(), data.password());
            case "ADMIN" -> adminService.registry(data.login(), data.password());
            default -> Mono.just(ResponseEntity.ok().body(new RegistrationResponse(List.of("Account type is not detected"))));
        };
    }


}
