package com.example.Tasksat.data.entities.accounts.users;

import com.example.Tasksat.data.entities.accounts.Account;
import com.example.Tasksat.data.entities.tasks.Task;
import com.example.Tasksat.data.entities.tasks.TaskType;
import com.example.Tasksat.data.entities.tasks.question.QuestionTask;
import com.example.Tasksat.data.entities.tasks.recommendation.RecommendationTask;
import com.example.Tasksat.data.entities.tasks.test.TestTask;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import static com.example.Tasksat.data.entities.accounts.Authority.LOCKED;
import static com.example.Tasksat.data.entities.accounts.Authority.USER;

import java.util.HashSet;
import java.util.Set;

@Document("users")
@Data
@AllArgsConstructor
public class User extends Account {

    @Indexed(unique = true)
    private String email;

    @Indexed(unique = true)
    private String code;

    private Set<QuestionTask> completedQuestionTasks = new HashSet<>();

    private Set<RecommendationTask> completedRecommendationTasks = new HashSet<>();

    private Set<TestTask> completedTestTasks = new HashSet<>();

    public User() {
        authorities.add(LOCKED);
    }

    public void addCompletedTask(Task task) {

        switch (TaskType.valueOf(task.getType())) {
            case QUESTION -> completedQuestionTasks.add((QuestionTask) task);
            case RECOMMENDATION -> completedRecommendationTasks.add((RecommendationTask) task);
            case TEST -> completedTestTasks.add((TestTask) task);
        }
    }
    public void unlock() {
        code = null;
        authorities.clear();
        authorities.add(USER);
    }

    public long getCompletedRecommendationCount() {
        return completedRecommendationTasks.size();
    }
    public long getCompletedQuestionCount() {
        return completedQuestionTasks.size();
    }
    public long getCompletedTestCount() {
        return completedTestTasks.size();
    }
}
