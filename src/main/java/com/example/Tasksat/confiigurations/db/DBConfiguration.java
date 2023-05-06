package com.example.Tasksat.confiigurations.db;

import com.example.Tasksat.data.entities.accounts.admins.AdminRepository;
import com.example.Tasksat.data.entities.accounts.users.UserRepository;
import com.example.Tasksat.data.entities.accounts.workers.WorkerRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(basePackageClasses = {
        UserRepository.class,
        WorkerRepository.class,
        AdminRepository.class
})
public class DBConfiguration {
}
