package com.example.Tasksat.data.entities.tasks.test;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TestTaskRepository extends ReactiveMongoRepository<TestTask, String> {
    Mono<Boolean> existsByTitle(String title);
}
