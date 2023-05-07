package com.example.Tasksat.handling.controllers.statistics;

import com.example.Tasksat.data.dto.statistics.StatisticsDTO;
import com.example.Tasksat.data.dto.statistics.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(allowedHeaders = "*", methods = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST}, allowCredentials = "true", originPatterns = "*")
@RequestMapping("/statistics")
@PreAuthorize("hasAuthority('USER')")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping
    public Mono<StatisticsDTO> getStatistics(@RequestHeader("Authorization") String token) {

        return statisticsService.computeStatistic(token);
    }

}
