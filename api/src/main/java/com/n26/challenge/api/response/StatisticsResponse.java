package com.n26.challenge.api.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class StatisticsResponse {
    private Double sum = 0D;
    private Double max = 0D;
    private Double min = 0D;
    private Long count = 0L;

    public Double getAvg() {
        return count == 0 ? 0 : (sum / count);
    }
}
