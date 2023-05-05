package com.example.Tasksat.confiigurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(basePackageClasses = AccountRepository.class)
public class DBConfiguration {
}
