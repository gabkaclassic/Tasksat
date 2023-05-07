package com.example.Tasksat.data.entities.tasks;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
public abstract class Task {

    @Field
    protected String type;

    protected abstract boolean checkAnswer(String answer);
}
