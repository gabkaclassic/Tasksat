package com.example.Tasksat.data.entities.accounts.admins;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface AdminRepository extends ReactiveMongoRepository<Admin, String> {

    Mono<UserDetails> findByLogin(String login);

    Mono<Boolean> existsByLogin(String login);

}
