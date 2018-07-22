package com.n26.challenge.web.controller;

import com.n26.challenge.api.StatisticsApi;
import com.n26.challenge.api.response.StatisticsResponse;
import com.n26.challenge.service.StatisticsService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsController implements StatisticsApi {

    private StatisticsService statisticsService;
    private ModelMapper modelMapper;

    public StatisticsController(StatisticsService statisticsService, ModelMapper modelMapper) {
        this.statisticsService = statisticsService;
        this.modelMapper = modelMapper;
    }

    @Override
    public StatisticsResponse getStatisticsOfLastMinute() {
        return modelMapper.map(statisticsService.getStatisticsOfLastMinute(), StatisticsResponse.class);
    }
}
