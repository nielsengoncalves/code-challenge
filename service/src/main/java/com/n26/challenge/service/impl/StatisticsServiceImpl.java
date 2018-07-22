package com.n26.challenge.service.impl;

import com.n26.challenge.model.TransactionStatistics;
import com.n26.challenge.repository.TransactionRepository;
import com.n26.challenge.service.StatisticsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private TransactionRepository transactionRepository;

    StatisticsServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public TransactionStatistics getStatisticsOfLastMinute() {
        TransactionStatistics transactionStatistics = new TransactionStatistics();
        transactionRepository.getComputedTransactions().forEach((key, value) -> {
            if (!value.getTime().isBefore(getLastMinuteTime())) {
                transactionStatistics.compute(value);
            }
        });
        return transactionStatistics;
    }

    private LocalDateTime getLastMinuteTime() {
        return LocalDateTime.now().minusSeconds(60);
    }
}
