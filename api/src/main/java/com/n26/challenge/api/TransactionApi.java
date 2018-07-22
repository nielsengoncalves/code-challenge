package com.n26.challenge.api;

import com.n26.challenge.api.request.SaveTransactionRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public interface TransactionApi {

    @ResponseStatus(CREATED)
    @PostMapping(value = "/transactions", consumes = APPLICATION_JSON_VALUE)
    void save(@Valid @RequestBody SaveTransactionRequest request);
}
