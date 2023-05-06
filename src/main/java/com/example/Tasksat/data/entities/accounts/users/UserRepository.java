package com.example.Tasksat.data.entities.accounts.users;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {

    Mono<UserDetails> findByLogin(String login);

    Mono<User> findByCode(String code);

    Mono<Boolean> existsByLogin(String login);

    Mono<Boolean> existsByLoginOrEmail(String login, String email);
}
