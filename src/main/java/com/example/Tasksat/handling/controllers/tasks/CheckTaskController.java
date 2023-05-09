package com.example.Tasksat.handling.controllers.tasks;

import com.example.Tasksat.data.dto.tasks.TaskDTO;
import com.example.Tasksat.data.dto.tasks.TestTaskDTO;
import com.example.Tasksat.data.entities.tasks.TaskService;
import com.example.Tasksat.handling.responses.SuccessOperationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@CrossOrigin(allowedHeaders = "*", methods = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST}, allowCredentials = "true", originPatterns = "*")
@RequestMapping("/check")
@PreAuthorize("hasAuthority('USER')")
@RequiredArgsConstructor
public class CheckTaskController {

    private final TaskService taskService;
    @GetMapping("/task")
    public Mono<ResponseEntity<SuccessOperationResponse>> checkTask(
            @RequestParam String taskId,
            @RequestParam String taskType,
            @RequestParam String answer,
            @RequestHeader("Authorization") String token) {

        return taskService.check(taskId, taskType, answer, token);
    }

    @GetMapping("/all/test")
    public Mono<ResponseEntity<List<TestTaskDTO>>>allTestTasks(@RequestHeader("Authorization") String token) {

        return taskService.allTestTask(token);
    }

    @GetMapping("/all/question")
    public Mono<ResponseEntity<List<TaskDTO>>> allQuestionTasks(@RequestHeader("Authorization") String token) {

        return taskService.allQuestionTask(token);
    }
    @GetMapping("/all/recommendation")
    public Mono<ResponseEntity<List<TaskDTO>>> allRecommendationTasks(@RequestHeader("Authorization") String token) {

        return taskService.allRecommendationTask(token);
    }
}
