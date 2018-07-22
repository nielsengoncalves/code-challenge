package com.n26.challenge.model

import spock.lang.Specification

import java.time.LocalDateTime

class ComputedTransactionsTest extends Specification {

    def "when creating a new ComputedTransactions all fields should be zeroed"() {
        when:
        def newComputedTransactions = new ComputedTransactions()

        then:
        newComputedTransactions.getTime() == null
        newComputedTransactions.getMax() == 0
        newComputedTransactions.getMin() == 0
        newComputedTransactions.getCount() == 0
    }

    def "when adding new transaction to ComputedTransactions, the fields must be updated"() {
        given:
        def computedTransactions = new ComputedTransactions()
        def currentTime = LocalDateTime.now().withNano(0)
        def transaction = Transaction.builder()
                .id(UUID.randomUUID())
                .amount(10)
                .time(currentTime)
                .build()

        when:
        computedTransactions.computeTransaction(transaction)

        then:
        computedTransactions.getTime() == currentTime
        computedTransactions.getMax() == 10
        computedTransactions.getMin() == 10
        computedTransactions.getCount() == 1
    }

    def "when adding multiple transactions to ComputedTransactions, the fields must be updated"() {
        given:
        def computedTransactions = new ComputedTransactions()
        def currentTime = LocalDateTime.now().withNano(0)
        def minTransaction = Transaction.builder()
                .id(UUID.randomUUID())
                .amount(2)
                .time(currentTime)
                .build()
        def maxTransaction = Transaction.builder()
                .id(UUID.randomUUID())
                .amount(10)
                .time(currentTime)
                .build()
        computedTransactions.computeTransaction(minTransaction)


        when:
        computedTransactions.computeTransaction(maxTransaction)

        then:
        computedTransactions.getTime() == currentTime
        computedTransactions.getMax() == 10
        computedTransactions.getMin() == 2
        computedTransactions.getCount() == 2
    }
}