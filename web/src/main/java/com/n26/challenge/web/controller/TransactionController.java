package com.n26.challenge.web.controller;

import com.n26.challenge.api.TransactionApi;
import com.n26.challenge.api.request.SaveTransactionRequest;
import com.n26.challenge.model.Transaction;
import com.n26.challenge.service.TransactionService;
import com.n26.challenge.service.exception.FutureTransactionException;
import com.n26.challenge.service.exception.OldTransactionException;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
public class TransactionController implements TransactionApi {

    private TransactionService transactionService;
    private ModelMapper modelMapper;

    TransactionController(TransactionService transactionService, ModelMapper modelMapper) {
        this.transactionService = transactionService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void save(@Valid @RequestBody SaveTransactionRequest request) {
        Transaction transaction = convertToTransactionModel(request);
        transactionService.save(transaction);
    }

    private Transaction convertToTransactionModel(SaveTransactionRequest request) {
        Transaction transaction = modelMapper.map(request, Transaction.class);
        Instant instant = Instant.ofEpochMilli(request.getTimestamp());
        LocalDateTime time = LocalDateTime.ofInstant(instant, TimeZone.getDefault().toZoneId());
        transaction.setTime(time);
        return transaction;
    }

    @ExceptionHandler({ OldTransactionException.class, FutureTransactionException.class })
    private ResponseEntity<String> handle() {
        return new ResponseEntity<>(NO_CONTENT);
    }
}
