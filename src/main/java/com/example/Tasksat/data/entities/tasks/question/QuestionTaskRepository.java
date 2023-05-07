package com.example.Tasksat.data.entities.tasks.question;

import com.example.Tasksat.data.entities.accounts.workers.Worker;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface QuestionTaskRepository extends ReactiveMongoRepository<QuestionTask, String> {
    Mono<Boolean> existsByTitle(String title);
}
