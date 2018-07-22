package com.n26.challenge.service.impl

import com.n26.challenge.model.ComputedTransactions
import com.n26.challenge.model.Transaction
import com.n26.challenge.repository.TransactionRepository
import spock.lang.Specification

import java.time.LocalDateTime

class StatisticsServiceImplTest extends Specification {

    def transactionRepository = Mock(TransactionRepository)
    def statisticsService = new StatisticsServiceImpl(transactionRepository)

    def "should only get statistics of transactions in last minute"() {
        given:
        def computedTransactions = new HashMap<Integer, ComputedTransactions>()
        def validComputedTransaction = new ComputedTransactions()
        def invalidComputedTransaction = new ComputedTransactions()
        def transactionInLastMinute = Transaction.builder()
                .id(UUID.randomUUID())
                .amount(10)
                .time(LocalDateTime.now())
                .build()
        def transactionInLastHour = Transaction.builder()
                .id(UUID.randomUUID())
                .amount(100)
                .time(LocalDateTime.now().minusHours(1).minusSeconds(5))
                .build()
        validComputedTransaction.computeTransaction(transactionInLastMinute)
        invalidComputedTransaction.computeTransaction(transactionInLastHour)

        computedTransactions.put(0, validComputedTransaction)
        computedTransactions.put(1, invalidComputedTransaction)

        when:
        def statistics = statisticsService.getStatisticsOfLastMinute()

        then:
        transactionRepository.getComputedTransactions() >> computedTransactions
        with(statistics) {
            count == 1
            max == 10
            min == 10
            sum == 10
        }
    }

}