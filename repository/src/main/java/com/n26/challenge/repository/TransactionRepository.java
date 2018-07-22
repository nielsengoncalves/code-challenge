package com.n26.challenge.repository;

import com.n26.challenge.model.ComputedTransactions;
import com.n26.challenge.model.Transaction;

import java.util.Map;

public interface TransactionRepository {

    void compute(Transaction transaction);

    Map<Integer, ComputedTransactions> getComputedTransactions();

}
