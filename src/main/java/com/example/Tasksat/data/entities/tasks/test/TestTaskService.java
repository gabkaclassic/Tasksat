package com.example.Tasksat.data.entities.tasks.test;


import com.example.Tasksat.data.dto.tasks.TestTaskDTO;
import com.example.Tasksat.data.entities.accounts.users.UserService;
import com.example.Tasksat.data.entities.tasks.TaskChecker;
import com.example.Tasksat.handling.responses.SuccessOperationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
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
                        userService.findById(userId).doOnNext(user -> {
                            user.addCompletedTask(task);
                        }).subscribe(userService::save);

                    }

                    return res;
                });
    }

    public Mono<ResponseEntity<SuccessOperationResponse>> create(String title, String variant1, String variant2, String variant3, String rightAnswer, String description) {

        return repository.existsByTitle(title).map(res -> {

            if(res)
                return ResponseEntity.ok(new SuccessOperationResponse(false));

            var variants = Map.of("1", variant1, "2", variant2, "3", variant3);
            var task = new TestTask(description, title, rightAnswer, variants);
            repository.save(task).subscribe();

            return ResponseEntity.ok(new SuccessOperationResponse(true));
        });
    }

    public Mono<List<TestTaskDTO>> allLikeDTO(Set<TestTask> excludes) {

        return repository.findAll()
                .filter(task -> !excludes.contains(task))
                .take(50, true)
                .map(task -> new TestTaskDTO(task.getTitle(), task.getDescription(), task.getId(), task.getType(), task.getVariants()))
                .collectList();
    }

    public Mono<Long> countAll() {
        return repository.count();
    }
}
