package com.n26.challenge.repository.impl

import com.n26.challenge.model.Transaction
import spock.lang.Specification

import java.time.LocalDateTime

import static java.time.ZoneOffset.*

class InMemoryTransactionRepositoryImplTest extends Specification {

    def repository = new InMemoryTransactionRepositoryImpl()

    def setup() {
        TimeZone.setDefault(TimeZone.getTimeZone(UTC))
    }

    def "should test that concurrentHashMap can't go over 60 positions, one for each second"() {
        given:
        List<Transaction> transactions = buildOneTransactionForEverySecond(100)
        transactions.forEach { transaction -> repository.compute(transaction) }

        when:
        def hashMap = repository.getLastComputedTransactions()

        then:
        hashMap.size() == 60
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