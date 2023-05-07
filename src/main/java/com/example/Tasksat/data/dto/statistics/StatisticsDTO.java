package com.example.Tasksat.data.dto.statistics;

import lombok.Data;

@Data
public class StatisticsDTO {

    private long usersCount;
    private long recommendationTasksCount;
    private long questionTasksCount;
    private long testTasksCount;
    private long completedRecommendationTasksCount;
    private long completedQuestionTasksCount;
    private long completedTestTasksCount;
    private long generalCompletedRecommendationTasksCount;
    private long generalCompletedQuestionTasksCount;
    private long generalCompletedTestTasksCount;
}
