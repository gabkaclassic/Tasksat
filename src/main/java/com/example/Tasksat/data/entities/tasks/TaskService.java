package com.example.Tasksat.data.entities.tasks;

import com.example.Tasksat.data.dto.tasks.TaskDTO;
import com.example.Tasksat.data.dto.tasks.TestTaskDTO;
import com.example.Tasksat.data.entities.accounts.users.UserService;
import com.example.Tasksat.data.entities.tasks.question.QuestionTask;
import com.example.Tasksat.data.entities.tasks.question.QuestionTaskService;
import com.example.Tasksat.data.entities.tasks.recommendation.RecommendationTask;
import com.example.Tasksat.data.entities.tasks.recommendation.RecommendationTaskService;
import com.example.Tasksat.data.entities.tasks.test.TestTask;
import com.example.Tasksat.data.entities.tasks.test.TestTaskService;
import com.example.Tasksat.handling.responses.SuccessOperationResponse;
import com.example.Tasksat.handling.utils.JWTUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    @Getter
    private final RecommendationTaskService recommendationTaskService;

    @Getter
    private final QuestionTaskService questionTaskService;

    @Getter
    private final TestTaskService testTaskService;

    private final JWTUtil jwtUtil;

    private final UserService userService;
    public Mono<ResponseEntity<SuccessOperationResponse>> check(String taskId, String taskType, String answer, String token) {

        var userId = jwtUtil.extractId(token);

        var success = switch (TaskType.valueOf(taskType)) {
            case RECOMMENDATION -> recommendationTaskService.checkAnswer(taskId, answer, userId);
            case TEST -> testTaskService.checkAnswer(taskId, answer, userId);
            case QUESTION -> questionTaskService.checkAnswer(taskId, answer, userId);
        };

        return success.map(res -> ResponseEntity.ok(new SuccessOperationResponse(res)));
    }

    public Mono<ResponseEntity<List<TestTaskDTO>>> allTestTask(String token) {

        var excludes = new HashSet<TestTask>();
        return userService.findById(jwtUtil.extractId(token))
                .doOnNext(user -> excludes.addAll(user.getCompletedTestTasks()))
                .then(testTaskService.allLikeDTO(excludes))
                .map(ResponseEntity::ok);
    }

    public Mono<ResponseEntity<List<TaskDTO>>> allRecommendationTask(String token) {

        var excludes = new HashSet<RecommendationTask>();
        return userService.findById(jwtUtil.extractId(token))
                .doOnNext(user -> excludes.addAll(user.getCompletedRecommendationTasks()))
                .then(recommendationTaskService.allLikeDTO(excludes))
                .map(ResponseEntity::ok);
    }

    public Mono<ResponseEntity<List<TaskDTO>>> allQuestionTask(String token) {

        var excludes = new HashSet<QuestionTask>();
        return userService.findById(jwtUtil.extractId(token))
                .doOnNext(user -> excludes.addAll(user.getCompletedQuestionTasks()))
                .then(questionTaskService.allLikeDTO(excludes))
                .map(ResponseEntity::ok);
    }
}
