package com.jobtrace.stats.service;

import com.jobtrace.domain.User;
import com.jobtrace.global.common.StatusType;
import com.jobtrace.repository.ApplicationStatusRepository;
import com.jobtrace.repository.JobPostingRepository;
import com.jobtrace.stats.dto.StatsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.LinkedHashMap;
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
        long totalApplied = applicationStatusRepository.countApplied(user, StatusType.BOOKMARKED);
        long bookmarkedCount = applicationStatusRepository.countByLatestStatusIn(user, List.of(StatusType.BOOKMARKED));
        String insight = getInsight(totalApplied, bookmarkedCount, documentPassRate,finalPassRate);

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

        //1. 관심공고 제외 지원한 공고 수 가져오기
        long  totalApplied = applicationStatusRepository.countApplied(user, StatusType.BOOKMARKED);

        //2. 서류 합격 공고 수
        long countDocumentPassed = applicationStatusRepository.countByLatestStatusIn(user, List.of(StatusType.DOCUMENT_PASS));

        //3. 나눠서 비율 계산
        if(totalApplied == 0) return 0.0; // 전체지원이 0일 때 0으로 나누기 방지

        return (double) countDocumentPassed / totalApplied * 100;
    }
    private double getFinalPassRate(User user){
    //1. 최종 합격수
        long totalFinalPass = applicationStatusRepository.countByLatestStatusIn(user, List.of(StatusType.FINAL_PASS));
        //2. 총 면접수

        List<String> interviewStatuses = List.of(
                StatusType.INTERVIEW_1,
                StatusType.INTERVIEW_2,
                StatusType.AI_INTERVIEW,
                StatusType.FINAL_PASS
        );
        long interviewCount = applicationStatusRepository.countByLatestStatusIn(user, interviewStatuses);

        //3. 나눠서 비율 계산
        if(interviewCount == 0) return 0.0;

        return (double) totalFinalPass/ interviewCount * 100;

    }
    //월별 지원수
    private Map <String, Long> getMonthlyCount(User user){
        List<Object[]> results = jobPostingRepository.countMonthly(user);
        Map <String ,Long> monthlyCount = new LinkedHashMap<>();
        for(Object[] row: results){
            String month = (String) row[0];
            Long count = (Long) row[1];
            monthlyCount.put(month, count);
        }
        return monthlyCount;
     }
    private String getInsight(long totalApplied,long bookmarkedCount, double documentPassRate, double finalPassRate) {

        // 1. 관심공고는 많은데 지원이 적으면
        if (bookmarkedCount > 5 && totalApplied < 3) {
            return "저장한 공고가 많네요! 지원을 시작해보세요.";
        }

        // 2. 지원 수 자체가 적으면
        if (totalApplied < 5) {
            return "지원 수가 부족해요. 더 많은 곳에 지원해보세요!";
        }

        // 3. 서류합격률이 낮으면
        if (documentPassRate < 20) {
            return "서류 합격률이 낮아요. 이력서를 다듬어보세요!";
        }

        // 4. 서류는 되는데 최종합격률이 낮으면
        if (documentPassRate >= 20 && finalPassRate < 20) {
            return "면접에서 아쉬움이 있어요. 면접 준비를 강화해보세요!";
        }

        return "잘 하고 있어요! 꾸준히 지원해보세요 💪";
    }


}
