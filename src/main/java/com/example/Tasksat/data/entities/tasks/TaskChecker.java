package com.example.Tasksat.data.entities.tasks;

import reactor.core.publisher.Mono;

public interface TaskChecker {

    Mono<Boolean> checkAnswer(String id, String answer, String userId);
}
