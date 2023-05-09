package com.example.Tasksat.data.dto.statistics;

import com.example.Tasksat.data.entities.accounts.users.UserService;
import com.example.Tasksat.data.entities.tasks.TaskService;
import com.example.Tasksat.handling.utils.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final UserService userService;

    private final JWTUtil jwtUtil;

    private final TaskService taskService;

    @Cacheable(value = "statistics", unless = "#result == null")
    public Mono<StatisticsDTO> computeStatistic(String token) {

        var statistics = new StatisticsDTO();

        return userService.findById(jwtUtil.extractId(token))
                .doOnNext(user -> {
                    statistics.setCompletedTestTasksCount(user.getCompletedTestCount());
                    statistics.setCompletedRecommendationTasksCount(user.getCompletedRecommendationCount());
                    statistics.setCompletedQuestionTasksCount(user.getCompletedQuestionCount());
                })
                .then(userService.countAll())
                .doOnNext(statistics::setUsersCount)

                .then(taskService.getRecommendationTaskService().countAll())
                .doOnNext(statistics::setRecommendationTasksCount)
                .then(taskService.getQuestionTaskService().countAll())
                .doOnNext(statistics::setQuestionTasksCount)
                .then(taskService.getTestTaskService().countAll())
                .doOnNext(statistics::setTestTasksCount)

                .then(userService.countRecommendationCompletedTask())
                .doOnNext(statistics::setGeneralCompletedRecommendationTasksCount)
                .then(userService.countQuestionCompletedTask())
                .doOnNext(statistics::setGeneralCompletedQuestionTasksCount)
                .then(userService.countRecommendationCompletedTask())
                .doOnNext(statistics::setGeneralCompletedTestTasksCount)
                .then(Mono.just(statistics));
    }
}
