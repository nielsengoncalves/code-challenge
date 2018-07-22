package com.n26.challenge.api;

import com.n26.challenge.api.response.StatisticsResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public interface StatisticsApi {

    @ResponseStatus(OK)
    @GetMapping(value = "/statistics", produces = APPLICATION_JSON_VALUE)
    StatisticsResponse getStatisticsOfLastMinute();

}
