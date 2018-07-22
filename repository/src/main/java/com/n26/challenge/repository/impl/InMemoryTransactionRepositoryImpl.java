package com.n26.challenge.repository.impl;

import com.n26.challenge.model.ComputedTransactions;
import com.n26.challenge.model.Transaction;
import com.n26.challenge.repository.TransactionRepository;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryTransactionRepositoryImpl implements TransactionRepository {

    private ConcurrentHashMap<Integer, ComputedTransactions> computedTransactions = new ConcurrentHashMap<>();

    @Override
    public void compute(Transaction transaction) {
        Integer index = getHashIndex(transaction);

        computedTransactions.compute(index, (key, record) -> {
            if (record == null || isTransactionMoreRecent(transaction, record)) {
                return getNewRecord(transaction);
            } else if (isTransactionInTheSameSecond(transaction, record)) {
                return getUpdatedFoundRecord(transaction, record);
            }
            return record;
        });
    }

    @Override
    public ConcurrentHashMap<Integer, ComputedTransactions> getComputedTransactions() {
        return computedTransactions;
    }

    private ComputedTransactions getUpdatedFoundRecord(Transaction transaction, ComputedTransactions foundRecord) {
        foundRecord.computeTransaction(transaction);
        return foundRecord;
    }

    private ComputedTransactions getNewRecord(Transaction transaction) {
        ComputedTransactions newRecord = new ComputedTransactions();
        newRecord.computeTransaction(transaction);
        return newRecord;
    }

    private Boolean isTransactionMoreRecent(Transaction transaction, ComputedTransactions foundRecord) {
        return transaction.getTime().isAfter(foundRecord.getTime());
    }

    private Boolean isTransactionInTheSameSecond(Transaction transaction, ComputedTransactions found) {
        return transaction.getTime().isEqual(found.getTime());
    }

    private Integer getHashIndex(Transaction transaction) {
        return transaction.getTime().getSecond() % 60;
    }

}
