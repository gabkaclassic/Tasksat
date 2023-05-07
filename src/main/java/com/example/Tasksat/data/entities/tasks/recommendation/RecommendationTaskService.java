package com.example.Tasksat.data.entities.tasks.recommendation;


import com.example.Tasksat.data.dto.tasks.TaskDTO;
import com.example.Tasksat.data.entities.accounts.users.UserService;
import com.example.Tasksat.data.entities.tasks.TaskChecker;
import com.example.Tasksat.data.entities.tasks.test.TestTask;
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
public class RecommendationTaskService implements TaskChecker {

    private final RecommendationTaskRepository repository;
    private final UserService userService;

    @Override
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

    public Mono<ResponseEntity<CreateTaskResponse>> create(String title, String description) {
        return repository.existsByTitle(title).map(res -> {
            if(res)
                return ResponseEntity.ok(new CreateTaskResponse(false));

            var task = new RecommendationTask(description, title);
            repository.save(task).subscribe();

            return ResponseEntity.ok(new CreateTaskResponse(true));
        });
    }

    public Flux<TaskDTO> allLikeDTO(Set<RecommendationTask> excludes) {

        return repository.findAll()
                .filter(task -> !excludes.contains(task))
                .take(50)
                .map(task -> new TaskDTO(task.getTitle(), task.getDescription(), task.getId(), task.getType()));
    }

    public Mono<Long> countAll() {
        return repository.count();
    }
}
