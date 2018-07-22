package com.n26.challenge.service.impl

import com.n26.challenge.model.Transaction
import com.n26.challenge.repository.TransactionRepository
import com.n26.challenge.service.exception.FutureTransactionException
import com.n26.challenge.service.exception.OldTransactionException
import spock.lang.Specification

import java.time.LocalDateTime

import static java.time.ZoneOffset.*

class TransactionServiceImplTest extends Specification {

    def transactionRepository = Mock(TransactionRepository)
    def transactionService = new TransactionServiceImpl(transactionRepository)

    def setup() {
        TimeZone.setDefault(TimeZone.getTimeZone(UTC))
    }

    def "should save the transaction when it happened in the last 60 seconds"() {
        given:
        def validTime = LocalDateTime.now(UTC).minusSeconds(59)
        def validTransaction = Transaction.builder().time(validTime).build()

        when:
        transactionService.save(validTransaction)

        then:
        1 * transactionRepository.compute(validTransaction)
        noExceptionThrown()
    }

    def "should not save the transaction it is older than 60 seconds"() {
        given:
        def oldTime = LocalDateTime.now(UTC).minusSeconds(60)
        def oldTransaction = Transaction.builder().time(oldTime).build()
        when:
        transactionService.save(oldTransaction)

        then:
        0 * transactionRepository.compute(_)
        thrown(OldTransactionException)
    }

    def "should not save the transaction it is in the future"() {
        given:
        def futureTime = LocalDateTime.now(UTC).plusSeconds(1)
        def futureTransaction = Transaction.builder().time(futureTime).build()
        when:
        transactionService.save(futureTransaction)

        then:
        0 * transactionRepository.compute(_)
        thrown(FutureTransactionException)
    }

}