package com.n26.challenge.api.response

import spock.lang.Specification

class StatisticsResponseTest extends Specification {

    def "should validate that average is zero when StatisticsResponse is new"() {
        when:
        def statisticsResponse = new StatisticsResponse()

        then:
        statisticsResponse.avg == 0
    }

    def "should validate that StatisticsResponse average is being calculated correctly"() {
        when:
        def statisticsResponse = new StatisticsResponse(150, 100, 50, 2)

        then:
        statisticsResponse.avg == 75
    }
}