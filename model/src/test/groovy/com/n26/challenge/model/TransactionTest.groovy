package com.n26.challenge.model

import spock.lang.Specification
import java.time.LocalDateTime

class TransactionTest extends Specification {

    def "when setting Transaction time, than should remove nano"() {
        given:
        def transaction = new Transaction()
        def time = LocalDateTime.now()

        when:
        transaction.setTime(time)

        then:
        transaction.getTime() == time.withNano(0)
    }

}