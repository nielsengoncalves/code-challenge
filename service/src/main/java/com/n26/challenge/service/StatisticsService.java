package com.n26.challenge.service;

import com.n26.challenge.model.TransactionStatistics;

public interface StatisticsService {

    TransactionStatistics getStatisticsOfLastMinute();

}
