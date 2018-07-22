package com.n26.challenge.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
public class ComputedTransactions {

    private LocalDateTime time = null;
    private Long count = 0L;
    private Double sum = 0D;
    private Double min = 0D;
    private Double max = 0D;

    public void computeTransaction(Transaction transaction) {
        Double transactionAmount = transaction.getAmount();
        time = transaction.getTime();
        count += 1;
        sum += transactionAmount;
        defineMin(transactionAmount);
        defineMax(transactionAmount);
    }

    private void defineMin(Double min) {
        if (this.min == 0) {
            this.min = min;
        }
        this.min = Math.min(this.min, min);
    }

    private void defineMax(Double max) {
        this.max = Math.max(this.max, max);
    }

}
