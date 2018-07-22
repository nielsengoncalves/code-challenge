package com.n26.challenge.service.exception;

public class BusinessException extends RuntimeException {
    BusinessException(String message) {
        super(message);
    }
}
