package com.example.Tasksat.handling.controllers;

import com.example.Tasksat.data.dto.accounts.AccountDTO;
import com.example.Tasksat.data.entities.accounts.AccountService;
import com.example.Tasksat.handling.responses.SuccessOperationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RequestMapping("/admin")
@CrossOrigin(allowedHeaders = "*", methods = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE}, allowCredentials = "true", originPatterns = "*")
@RestController
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final AccountService accountService;

    @GetMapping
    public Mono<ResponseEntity<List<AccountDTO>>> allUsers() {
        return accountService.all();
    }

    @DeleteMapping
    public Mono<ResponseEntity<SuccessOperationResponse>> deleteUser(@RequestParam String type, @RequestParam String id) {
        return accountService.deleteAccount(id, type);
    }
}
