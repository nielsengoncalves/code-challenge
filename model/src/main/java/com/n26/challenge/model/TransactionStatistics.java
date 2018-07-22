package com.n26.challenge.model;

import lombok.Getter;

@Getter
public class TransactionStatistics {
    private Double sum = 0D;
    private Double max = 0D;
    private Double min = 0D;
    private Long count = 0L;

    public void compute(ComputedTransactions computedTransactions) {
        sum += computedTransactions.getSum();
        defineMin(computedTransactions.getMin());
        defineMax(computedTransactions.getMax());
        count += computedTransactions.getCount();
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
