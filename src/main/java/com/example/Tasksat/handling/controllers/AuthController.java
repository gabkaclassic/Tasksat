package com.example.Tasksat.handling.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(allowedHeaders = "*", methods = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST}, allowCredentials = "true", originPatterns = "*")
@RequestMapping("/auth")
public class AuthController {
}
