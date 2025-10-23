package com.apr.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class TimeWindowParser {

    public static Instant parse(String input) {
        Instant now = Instant.now();

        switch (input.toLowerCase()) {
            case "1d":
                return now.minus(1, ChronoUnit.DAYS);
            case "7d":
                return now.minus(7, ChronoUnit.DAYS);
            case "30d":
                return now.minus(30, ChronoUnit.DAYS);
            case "90d":
                return now.minus(90, ChronoUnit.DAYS);
            case "over":
                // 'over' = 제한 없이 전부
                return Instant.EPOCH;
            default:
                // ISO-8601 문자열 처리
                try {
                    return Instant.parse(input);
                } catch (Exception e) {
                    throw new IllegalArgumentException("Invalid time window format: " + input);
                }
        }
    }
}
