package com.example.Tasksat.handling.controllers;

import com.example.Tasksat.data.entities.users.AccountService;
import com.example.Tasksat.handling.requests.AuthorizationRequest;
import com.example.Tasksat.handling.requests.RegistrationRequest;
import com.example.Tasksat.handling.responses.AuthorizationResponse;
import com.example.Tasksat.handling.responses.RegistrationResponse;
import lombok.RequiredArgsConstructor;
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
    public Mono<ResponseEntity<AuthorizationResponse>> login(@RequestBody AuthorizationRequest userData) {

        return accountService.login(userData.login(), userData.password());
    }

    @PostMapping
    public Mono<ResponseEntity<RegistrationResponse>> registration(@RequestBody RegistrationRequest userData) {

        return accountService.registry(userData.login(), userData.password(), userData.email());
    }

    @GetMapping("/confirm/{code}")
    public Mono<Void> confirmation(@PathVariable String code, ServerHttpResponse response, ServerHttpRequest request) throws IOException {

        accountService.confirm(code);
        var url = request.getHeaders().getFirst("referer");
        response.setStatusCode(HttpStatus.PERMANENT_REDIRECT);
        response.getHeaders().setLocation(URI.create(url == null ? AFTER_CONFIRM_URL : url));

        return response.setComplete();
    }
}
