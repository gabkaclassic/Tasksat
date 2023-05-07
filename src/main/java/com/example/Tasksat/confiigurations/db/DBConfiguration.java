package com.example.Tasksat.confiigurations.db;

import com.example.Tasksat.data.entities.accounts.admins.AdminRepository;
import com.example.Tasksat.data.entities.accounts.users.UserRepository;
import com.example.Tasksat.data.entities.accounts.workers.WorkerRepository;
import com.example.Tasksat.data.entities.tasks.question.QuestionTaskRepository;
import com.example.Tasksat.data.entities.tasks.recommendation.RecommendationTaskRepository;
import com.example.Tasksat.data.entities.tasks.test.TestTaskRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(basePackageClasses = {
        RecommendationTaskRepository.class,
        QuestionTaskRepository.class,
        TestTaskRepository.class,
        UserRepository.class,
        WorkerRepository.class,
        AdminRepository.class,
})
@Profile("container")
public class DBConfiguration {
}

