package com.example.Tasksat.handling.controllers;

import com.example.Tasksat.data.entities.accounts.AccountService;
import com.example.Tasksat.data.entities.accounts.admins.Admin;
import com.example.Tasksat.data.entities.accounts.users.User;
import com.example.Tasksat.data.entities.accounts.workers.Worker;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RequestMapping("/admin")
@CrossOrigin(allowedHeaders = "*", methods = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST}, allowCredentials = "true", originPatterns = "*")
@RestController
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final AccountService accountService;

    @GetMapping("/all/users")
    public Flux<User> allUsers() {
        return accountService.getUserService().all();
    }

    @GetMapping("/all/workers")
    public Flux<Worker> allWorkers() {
        return accountService.getWorkerService().all();
    }

    @GetMapping("/all/admins")
    public Flux<Admin> allAdmins() {
        return accountService.getAdminService().all();
    }
}
