package com.example.Tasksat.handling.controllers;

import com.example.Tasksat.data.entities.accounts.AccountService;
import com.example.Tasksat.handling.requests.AuthorizationRequest;
import com.example.Tasksat.handling.requests.RegistrationRequest;
import com.example.Tasksat.handling.responses.account.AuthorizationResponse;
import com.example.Tasksat.handling.responses.account.RegistrationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URI;

@RestController
@CrossOrigin(allowedHeaders = "*", methods = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST}, allowCredentials = "true", originPatterns = "*")
@RequestMapping("/auth")
public class AuthController {

    private final AccountService accountService;
    private final String AFTER_CONFIRM_URL;

    public AuthController(@Autowired AccountService accountService, @Value("${url.confirm}") String confirmUrl) {
        this.accountService = accountService;
        AFTER_CONFIRM_URL = confirmUrl;
    }

    @GetMapping
    public Mono<ResponseEntity<AuthorizationResponse>> login(
            @RequestParam String login,
            @RequestParam String password,
            @RequestParam String iam
    ) {

        return accountService.login(new AuthorizationRequest(login, password, iam));
    }

    @PostMapping
    public Mono<ResponseEntity<RegistrationResponse>> registration(
            @RequestParam String login,
            @RequestParam String password,
            @RequestParam(required = false) String email,
            @RequestParam String iam,
            @RequestHeader(value = "Authorization", required = false) String token
    ) {

        return accountService.registration(new RegistrationRequest(login, password, email, iam), token);
    }

    @GetMapping("/confirm/{code}")
    public Mono<Void> confirmation(@PathVariable String code, ServerHttpResponse response, ServerHttpRequest request) throws IOException {

        accountService.getUserService().confirm(code);
        var url = request.getHeaders().getFirst("referer");
        response.setStatusCode(HttpStatus.PERMANENT_REDIRECT);
        response.getHeaders().setLocation(URI.create(url == null ? AFTER_CONFIRM_URL : url));

        return response.setComplete();
    }
}
