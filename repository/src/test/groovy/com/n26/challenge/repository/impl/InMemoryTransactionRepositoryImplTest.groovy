package com.n26.challenge.repository.impl

import com.n26.challenge.model.ComputedTransactions
import com.n26.challenge.model.Transaction
import spock.lang.Specification

import java.time.LocalDateTime

import static java.time.ZoneOffset.*

class InMemoryTransactionRepositoryImplTest extends Specification {

    def repository = new InMemoryTransactionRepositoryImpl()

    def setup() {
        TimeZone.setDefault(TimeZone.getTimeZone(UTC))
    }

    def "should verify that concurrentHashMap can't go over 60 positions, one for each second mapped"() {
        given:
        List<Transaction> transactions = buildOneTransactionForEverySecond(100)
        transactions.forEach { transaction -> repository.compute(transaction) }

        when:
        def computedTransactions = repository.getComputedTransactions()

        then:
        computedTransactions.size() == 60
    }

    def "should save a new record on concurrentHashMap for a non mapped second"() {
        given:
        def time = LocalDateTime.now()
        def index = time.getSecond() % 60
        def transaction = Transaction.builder()
                .id(UUID.randomUUID())
                .amount(10)
                .time(time)
                .build()
        def expectedComputedTransaction = new ComputedTransactions()
        expectedComputedTransaction.computeTransaction(transaction)
        repository.compute(transaction)


        when:
        def computedTransactions = repository.getComputedTransactions()

        then:
        computedTransactions.get(index) == expectedComputedTransaction
    }

    def "should replace the saved record when it's already mapped but with older time"() {
        given:
        def now = LocalDateTime.now()
        def oldTime = now.minusMinutes(1)
        def index = now.getSecond() % 60
        def oldTransaction = Transaction.builder()
                .id(UUID.randomUUID())
                .amount(10)
                .time(oldTime)
                .build()
        def newerTransaction = Transaction.builder()
                .id(UUID.randomUUID())
                .amount(10)
                .time(now)
                .build()
        def expectedComputedTransaction = new ComputedTransactions()
        expectedComputedTransaction.computeTransaction(newerTransaction)
        repository.compute(oldTransaction)
        repository.compute(newerTransaction)

        when:
        def computedTransactions = repository.getComputedTransactions()

        then:
        now.getSecond() % 60 == oldTime.getSecond() % 60
        computedTransactions.get(index) == expectedComputedTransaction
    }

    def "should increase the saved record when it's in the same time"() {
        given:
        def now = LocalDateTime.now()
        def index = now.getSecond() % 60
        def transaction1 = Transaction.builder()
                .id(UUID.randomUUID())
                .amount(10)
                .time(now)
                .build()
        def transaction2 = Transaction.builder()
                .id(UUID.randomUUID())
                .amount(20)
                .time(now)
                .build()
        def expectedComputedTransaction = new ComputedTransactions()
        expectedComputedTransaction.computeTransaction(transaction1)
        expectedComputedTransaction.computeTransaction(transaction2)
        repository.compute(transaction1)
        repository.compute(transaction2)

        when:
        def computedTransactions = repository.getComputedTransactions()

        then:
        computedTransactions.get(index) == expectedComputedTransaction
    }

    private static buildOneTransactionForEverySecond(Integer total) {
        List<Transaction> transactions = new ArrayList<>()
        (1..total).each { i ->
            transactions.add(
                Transaction.builder()
                .id(UUID.randomUUID())
                .amount(10)
                .time(LocalDateTime.now().minusSeconds(i))
                .build()
            )
        }
        return transactions
    }
}