package com.jobtrace.stats.service;

import com.jobtrace.domain.User;
import com.jobtrace.repository.ApplicationStatusRepository;
import com.jobtrace.repository.JobPostingRepository;
import com.jobtrace.stats.dto.StatsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly= true)
public class StatsServiceImpl implements StatsService{
    private final ApplicationStatusRepository applicationStatusRepository;
    private final JobPostingRepository jobPostingRepository;

    @Override
    public StatsResponse getStats(User user) {
        Map<String, Long> statusCount = getStatusCount(user);
        double documentPassRate = getDocumentPassRate(user);
        double finalPassRate = getFinalPassRate(user);
        Map<String, Long> monthlyCount = getMonthlyCount(user);
        String insight = getInsight(statusCount, documentPassRate, finalPassRate);

        return StatsResponse.builder()
                .statusCount(statusCount)
                .documentPassRate(documentPassRate)
                .finalPassRate(finalPassRate)
                .monthlyCount(monthlyCount)
                .insight(insight)
                .build();
    }
    private Map<String,Long> getStatusCount(User user){
        List<Object[]> results = applicationStatusRepository.countByStatusLatest(user);
        Map<String, Long> statusCount = new HashMap<>();
        for(Object[] row: results) {
            String status = (String) row[0];
            Long count = (Long) row[1];
            statusCount.put(status, count);
        }
        return statusCount;
    }
    private double getDocumentPassRate(User user){


    }
    private double getFinalPassRate(User user){

    }
    private Map <String, Long> getMonthlyCount(User user){

    }
    private String getInsight(statusCount, documentPassRate, finalPassRate){

    }


}
