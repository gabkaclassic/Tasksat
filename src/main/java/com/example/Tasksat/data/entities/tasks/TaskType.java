package com.example.Tasksat.data.entities.tasks;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("task_types")
public class TaskType {

    @Id
    private String id;

    @Indexed(unique = true)
    private String title;

    private String description;
}
