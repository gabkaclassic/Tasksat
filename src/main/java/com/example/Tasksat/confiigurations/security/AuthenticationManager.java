package com.example.Tasksat.confiigurations.security;

import com.example.Tasksat.data.entities.accounts.users.UserService;
import com.example.Tasksat.handling.utils.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Primary
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final JWTUtil jwtUtil;
    @Bean
    protected ReactiveAuthenticationManager reactiveAuthenticationManager(UserService userDetailsService, PasswordEncoder passwordEncoder) {

        return authentication -> userDetailsService.findByUsername(authentication.getPrincipal().toString())
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found")))
                .flatMap(user -> {

                    final String username = authentication.getPrincipal().toString();
                    final CharSequence rawPassword = authentication.getCredentials().toString();

                    if(passwordEncoder.matches(rawPassword, user.getPassword())) {

                        return Mono.just( new UsernamePasswordAuthenticationToken(username, user.getPassword(), user.getAuthorities()) );
                    }

                    return Mono.just(UsernamePasswordAuthenticationToken.unauthenticated(authentication.getPrincipal(), authentication.getCredentials()));
                });
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {

        String authToken = authentication.getCredentials().toString();
        String username = jwtUtil.extractUsername(authToken);

        if(username == null || jwtUtil.validateToken(authToken))
            return Mono.empty();

        var auth = new UsernamePasswordAuthenticationToken(
                username,
                authentication.getCredentials(),
                jwtUtil.extractRoles(authToken)
        );

        return Mono.just(auth);
    }
}
