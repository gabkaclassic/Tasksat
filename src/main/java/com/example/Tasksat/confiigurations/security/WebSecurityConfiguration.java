package com.example.Tasksat.confiigurations.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {

    private final ReactiveAuthenticationManager manager;
    private final SecurityContextRepository repository;

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity security) throws Exception {

        security.cors().disable()
                .csrf().disable()
                .exceptionHandling()
                .accessDeniedHandler((exchange, denied) -> Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN)))
                .authenticationEntryPoint((exchange, ex) -> Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)))
                .and().formLogin().disable()
                .httpBasic().disable()
                .authenticationManager(manager)
                .securityContextRepository(repository)
                .authorizeExchange()
                .pathMatchers( "/auth/**", "/auth")
                .permitAll()
                .pathMatchers(HttpMethod.OPTIONS,
                        "/check/all/**",
                        "/check/task/**",
                        "/statistics/**"
                )
                .permitAll()
                .anyExchange().authenticated();

        return security.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder(8);
    }


}
