package com.n26.challenge.repository;

import com.n26.challenge.model.ComputedTransactions;
import com.n26.challenge.model.Transaction;

import java.util.concurrent.ConcurrentHashMap;

public interface TransactionRepository {

    void compute(Transaction transaction);

    ConcurrentHashMap<Integer, ComputedTransactions> getLastComputedTransactions();

}
