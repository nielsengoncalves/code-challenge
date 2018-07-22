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
        ComputedTransactions foundComputedTransactions = computedTransactions.get(index);

        if (isTransactionMoreRecent(transaction, foundComputedTransactions)) {
            computeNewRecord(transaction, index);
        } else if (isTransactionInTheSameSecond(transaction, foundComputedTransactions)) {
            updateFoundRecord(transaction, index, foundComputedTransactions);
        }
    }

    @Override
    public ConcurrentHashMap<Integer, ComputedTransactions> getLastComputedTransactions() {
        return computedTransactions;
    }

    private void updateFoundRecord(Transaction transaction, Integer index, ComputedTransactions foundRecord) {
        foundRecord.computeTransaction(transaction);
        computedTransactions.put(index, foundRecord);
    }

    private void computeNewRecord(Transaction transaction, Integer index) {
        ComputedTransactions newRecord = new ComputedTransactions();
        newRecord.computeTransaction(transaction);
        computedTransactions.put(index, newRecord);
    }

    private Boolean isTransactionMoreRecent(Transaction transaction, ComputedTransactions found) {
        return found == null || transaction.getTime().isAfter(found.getTime());
    }

    private Boolean isTransactionInTheSameSecond(Transaction transaction, ComputedTransactions found) {
        return transaction.getTime().isEqual(found.getTime());
    }

    private Integer getHashIndex(Transaction transaction) {
        return transaction.getTime().getSecond() % 60;
    }

}
