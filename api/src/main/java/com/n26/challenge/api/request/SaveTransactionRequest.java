package com.n26.challenge.api.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SaveTransactionRequest {
    @NotNull(message = "Amount must not be null.")
    @Positive(message = "Amount must be greater than 0.")
    private Double amount;

    @NotNull(message = "Timestamp must not be null.")
    private Long timestamp;
}