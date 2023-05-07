package com.example.Tasksat.data.entities.tasks.question;

import com.example.Tasksat.data.entities.tasks.Task;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import static com.example.Tasksat.data.entities.tasks.TaskType.QUESTION;

@Document("question_tasks")
@NoArgsConstructor
@Data
public class QuestionTask extends Task {

    @Id
    private String id;

    private String title;

    private String description;

    private String rightAnswer;

    public QuestionTask(
            String description,
            String title,
            String rightAnswer) {
        this.description = description;
        this.title = title;
        this.rightAnswer = rightAnswer;
        type = QUESTION.name();
    }

    public boolean checkAnswer(String answer) {
        return rightAnswer.equalsIgnoreCase(answer);
    }
}
