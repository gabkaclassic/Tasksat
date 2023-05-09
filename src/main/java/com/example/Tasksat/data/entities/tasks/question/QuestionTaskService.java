package com.example.Tasksat.data.entities.tasks.question;

import com.example.Tasksat.data.dto.tasks.TaskDTO;
import com.example.Tasksat.data.entities.accounts.users.UserService;
import com.example.Tasksat.data.entities.tasks.TaskChecker;
import com.example.Tasksat.handling.responses.SuccessOperationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class QuestionTaskService implements TaskChecker {

    private final QuestionTaskRepository repository;

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

    public Mono<ResponseEntity<SuccessOperationResponse>> create(String title, String rightAnswer, String description) {

        return repository.existsByTitle(title).map(res -> {

            if(res)
                return ResponseEntity.ok(new SuccessOperationResponse(false));

            var task = new QuestionTask(description, title, rightAnswer);
            repository.save(task).subscribe();

            return ResponseEntity.ok(new SuccessOperationResponse(true));
        });
    }

    public Mono<List<TaskDTO>> allLikeDTO(Set<QuestionTask> excludes) {

        return repository.findAll()
                .filter(task -> !excludes.contains(task))
                .take(50, true)
                .map(task -> new TaskDTO(task.getTitle(), task.getDescription(), task.getId(), task.getType()))
                .collectList();
    }

    public Mono<Long> countAll() {
        return repository.count();
    }
}
