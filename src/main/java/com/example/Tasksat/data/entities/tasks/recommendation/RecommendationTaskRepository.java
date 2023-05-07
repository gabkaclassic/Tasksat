package com.example.Tasksat.data.entities.tasks.recommendation;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface RecommendationTaskRepository extends ReactiveMongoRepository<RecommendationTask, String> {


    Mono<Boolean> existsByTitle(String title);
}
