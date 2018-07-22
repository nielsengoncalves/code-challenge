package com.n26.challenge.service.impl;

import com.n26.challenge.model.Transaction;
import com.n26.challenge.repository.TransactionRepository;
import com.n26.challenge.service.TransactionService;
import com.n26.challenge.service.exception.FutureTransactionException;
import com.n26.challenge.service.exception.OldTransactionException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionServiceImpl implements TransactionService {

    private TransactionRepository transactionRepository;

    TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void save(Transaction transaction) {
        validateTransactionTime(transaction);
        transactionRepository.compute(transaction);
    }

    private void validateTransactionTime(Transaction transaction) {
        validateIfTransactionIsOld(transaction);
        validateIfTransactionIsInTheFuture(transaction);
    }

    private void validateIfTransactionIsOld(Transaction transaction) {
        LocalDateTime lastMinute = getLastMinute();
        if (transaction.getTime().isBefore(lastMinute)) {
            throw new OldTransactionException("Can't compute transactions older than 60 seconds.");
        }
    }

    private void validateIfTransactionIsInTheFuture(Transaction transaction) {
        LocalDateTime now = LocalDateTime.now();
        if (transaction.getTime().isAfter(now)) {
            throw new FutureTransactionException("Can't compute transactions that happened in the future.");
        }
    }

    private LocalDateTime getLastMinute() {
        return LocalDateTime.now().minusSeconds(60);
    }
}
