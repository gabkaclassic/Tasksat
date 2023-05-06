package com.example.Tasksat.data.entities.tasks;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("tasks")
public class Task {
    @Id
    private String id;

    private String title;

    private String description;

    private TaskType type;
}
