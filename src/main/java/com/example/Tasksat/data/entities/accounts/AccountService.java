package com.example.Tasksat.data.entities.accounts;

import com.example.Tasksat.data.dto.accounts.AccountDTO;
import com.example.Tasksat.data.entities.accounts.admins.AdminService;
import com.example.Tasksat.data.entities.accounts.users.UserService;
import com.example.Tasksat.data.entities.accounts.workers.WorkerService;
import com.example.Tasksat.handling.requests.AuthorizationRequest;
import com.example.Tasksat.handling.requests.RegistrationRequest;
import com.example.Tasksat.handling.responses.SuccessOperationResponse;
import com.example.Tasksat.handling.responses.account.AuthorizationResponse;
import com.example.Tasksat.handling.responses.account.RegistrationResponse;
import com.example.Tasksat.handling.utils.JWTUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.example.Tasksat.data.entities.accounts.Authority.ADMIN;
import static com.example.Tasksat.data.entities.accounts.Authority.WORKER;


@Service
@RequiredArgsConstructor
public class AccountService {

    private final JWTUtil jwtUtil;

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

    public Mono<ResponseEntity<RegistrationResponse>> registration(RegistrationRequest data, String token) {

        if((data.iam().equals("ADMIN") || data.iam().equals("WORKER")) && token == null)
            return registrationErrorResponse("Authorization error");

        return switch (data.iam()) {

            case "USER" -> {
                if(data.email() == null)
                    yield registrationErrorResponse("Email has been required");

                yield userService.registry(data.login(), data.password(), data.email());
            }
            case "WORKER" -> {

                var roles = jwtUtil.extractRoles(token);

                if(!roles.contains(ADMIN) && !roles.contains(WORKER))
                    yield registrationErrorResponse("Permission denied");

                yield workerService.registry(data.login(), data.password());
            }
            case "ADMIN" -> {

                var roles = jwtUtil.extractRoles(token);

                if(!roles.contains(ADMIN))
                    yield registrationErrorResponse("Permission denied");

                yield adminService.registry(data.login(), data.password());
            }
            default -> registrationErrorResponse("Account type is not detected");
        };
    }

    private static Mono<ResponseEntity<RegistrationResponse>> registrationErrorResponse(String violation) {
        return Mono.just(ResponseEntity.ok().body(new RegistrationResponse(List.of(violation))));
    }

    public Mono<ResponseEntity<List<AccountDTO>>> all() {
        return Flux.merge(
                    userService.allLikeAccountDTO(),
                    workerService.allLikeAccountDTO(),
                    adminService.allLikeAccountDTO()
                )
                .collectList()
                .map(ResponseEntity::ok);
    }
    public Mono<ResponseEntity<SuccessOperationResponse>> deleteAccount(String id, String type) {

        return switch (Authority.valueOf(type.toUpperCase())) {
            case USER -> userService.deleteById(id);
            case ADMIN -> adminService.deleteById(id);
            case WORKER -> workerService.deleteById(id);
            default -> Mono.just(ResponseEntity.ok(new SuccessOperationResponse(false)));
        };
    }
}
