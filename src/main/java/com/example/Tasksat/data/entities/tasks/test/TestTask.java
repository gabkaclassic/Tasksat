package com.example.Tasksat.data.entities.tasks.test;

import com.example.Tasksat.data.entities.tasks.Task;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Map;

import static com.example.Tasksat.data.entities.tasks.TaskType.RECOMMENDATION;

@NoArgsConstructor
@Document("test_tasks")
@Data
public class TestTask extends Task {

    @Id
    private String id;

    private String title;

    private String description;

    private Map<String, String> variants;

    private String rightAnswer;
    public TestTask(
            String description,
            String title,
            String rightAnswer,
            Map<String, String> variants) {
        this.description = description;
        this.title = title;
        this.rightAnswer = rightAnswer;
        this.variants = variants;
        type = RECOMMENDATION.name();
    }

    @Override
    protected boolean checkAnswer(String answer) {
        return variants.get(rightAnswer).equalsIgnoreCase(answer);
    }
}
