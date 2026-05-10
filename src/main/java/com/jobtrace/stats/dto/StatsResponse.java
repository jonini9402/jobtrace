package com.jobtrace.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Map;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatsResponse {
    //퍼널
    private double documentPassRate;
    private double finalPassRate;

    //단계별 집계
    private Map<String, Long> statusCount;

    //월별 집계
    private Map<String, Long> monthlyCount;

    //해석+추천
    private String insight;
}
