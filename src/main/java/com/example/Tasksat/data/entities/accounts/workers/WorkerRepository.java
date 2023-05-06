package com.example.Tasksat.data.entities.accounts.workers;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface WorkerRepository extends ReactiveMongoRepository<Worker, String> {

    Mono<UserDetails> findByLogin(String login);

    Mono<Boolean> existsByLogin(String login);

}
