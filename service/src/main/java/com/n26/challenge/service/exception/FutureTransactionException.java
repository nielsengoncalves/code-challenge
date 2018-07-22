package com.n26.challenge.service.exception;

public class FutureTransactionException extends BusinessException {
    public FutureTransactionException(String message) {
        super(message);
    }
}
