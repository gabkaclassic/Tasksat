package com.example.Tasksat.handling.controllers.tasks;

import com.example.Tasksat.data.entities.tasks.TaskService;
import com.example.Tasksat.handling.responses.tasks.CreateTaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(allowedHeaders = "*", methods = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST}, allowCredentials = "true", originPatterns = "*")
@RequestMapping("/create")
@PreAuthorize("hasAuthority('WORKER')")
@RequiredArgsConstructor
public class CreateTaskController {
    
    private final TaskService taskService;
    @PostMapping("/test")
    public Mono<ResponseEntity<CreateTaskResponse>> createTestTask(
            @RequestParam String title,
            @RequestParam String variant1,
            @RequestParam String variant2,
            @RequestParam String variant3,
            @RequestParam String rightAnswer,
            @RequestParam String description
    ) {

        return taskService.getTestTaskService().create(title, variant1, variant2, variant3, rightAnswer, description);
    }

    @PostMapping("/question")
    public Mono<ResponseEntity<CreateTaskResponse>> createTestTask(
            @RequestParam String title,
            @RequestParam String rightAnswer,
            @RequestParam String description
    ) {

        return taskService.getQuestionTaskService().create(title, rightAnswer, description);
    }

    @PostMapping("/recommendation")
    public Mono<ResponseEntity<CreateTaskResponse>> createRecommendationTask(
            @RequestParam String title,
            @RequestParam String description
    ) {

        return taskService.getRecommendationTaskService().create(title, description);
    }
}
