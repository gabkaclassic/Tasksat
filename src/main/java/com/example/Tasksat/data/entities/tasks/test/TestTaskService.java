package com.example.Tasksat.data.entities.tasks.test;


import com.example.Tasksat.data.dto.tasks.TaskDTO;
import com.example.Tasksat.data.dto.tasks.TestTaskDTO;
import com.example.Tasksat.data.entities.accounts.users.UserService;
import com.example.Tasksat.data.entities.tasks.TaskChecker;
import com.example.Tasksat.data.entities.tasks.recommendation.RecommendationTask;
import com.example.Tasksat.handling.responses.tasks.CreateTaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TestTaskService implements TaskChecker {

    private final TestTaskRepository repository;

    private final UserService userService;
    public Mono<Boolean> checkAnswer(String id, String answer, String userId) {

        return repository.findById(id)
                .map(task -> {
                    var res = task.checkAnswer(answer);

                    if(res) {
                        userService.findById(userId).subscribe(user -> user.addCompletedTask(task));
                    }

                    return res;
                });
    }

    public Mono<ResponseEntity<CreateTaskResponse>> create(String title, String variant1, String variant2, String variant3, String rightAnswer, String description) {

        return repository.existsByTitle(title).map(res -> {

            if(res)
                return ResponseEntity.ok(new CreateTaskResponse(false));

            var variants = Map.of("1", variant1, "2", variant2, "3", variant3);
            var task = new TestTask(description, title, rightAnswer, variants);
            repository.save(task).subscribe();

            return ResponseEntity.ok(new CreateTaskResponse(true));
        });
    }

    public Flux<TestTaskDTO> allLikeDTO(Set<TestTask> excludes) {

        return repository.findAll()
                .filter(task -> !excludes.contains(task))
                .take(50)
                .map(task -> new TestTaskDTO(task.getTitle(), task.getDescription(), task.getId(), task.getType(), task.getVariants()));
    }

    public Mono<Long> countAll() {
        return repository.count();
    }
}
