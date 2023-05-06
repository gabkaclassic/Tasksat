package com.example.Tasksat.handling.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(allowedHeaders = "*", methods = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST}, allowCredentials = "true", originPatterns = "*")
@RequestMapping("/test")
public class TestController {

    @GetMapping
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Mono<Void> test() {
        System.out.println(")))");
        return Mono.empty();
    }
}
