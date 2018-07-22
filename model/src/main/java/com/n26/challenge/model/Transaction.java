package com.n26.challenge.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private UUID id;
    private Double amount;
    private LocalDateTime time;

    public void setTime(LocalDateTime time) {
        this.time = time.withNano(0);
    }

    public static class TransactionBuilder {
        public TransactionBuilder time(LocalDateTime time) {
            this.time = time.withNano(0);
            return this;
        }
    }
}
