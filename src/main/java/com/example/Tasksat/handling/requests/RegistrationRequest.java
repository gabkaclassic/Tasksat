package com.example.Tasksat.handling.requests;

import org.springframework.lang.Nullable;

public record RegistrationRequest(String login, String password, @Nullable String email, String iam) {
}
