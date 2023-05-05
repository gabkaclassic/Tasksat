package com.example.Tasksat.confiigurations.db;

import com.example.Tasksat.data.entities.users.AccountRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(basePackageClasses = AccountRepository.class)
public class DBConfiguration {
}
