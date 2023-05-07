package com.example.Tasksat.data.entities.tasks.recommendation;

import com.example.Tasksat.data.entities.tasks.Task;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import static com.example.Tasksat.data.entities.tasks.TaskType.RECOMMENDATION;
@Document("recommendation_tasks")
@NoArgsConstructor
@Data
public class RecommendationTask extends Task {

    @Id
    private String id;

    private String title;

    private String description;

    public RecommendationTask(
            String description,
            String title) {
        this.description = description;
        this.title = title;
        type = RECOMMENDATION.name();
    }

    @Override
    protected boolean checkAnswer(String answer) {
        return true;
    }
}
