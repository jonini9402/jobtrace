package com.jobtrace.job.service;

import com.jobtrace.domain.ApplicationStatus;
import com.jobtrace.domain.JobPosting;
import com.jobtrace.domain.User;
import com.jobtrace.global.common.StatusType;
import com.jobtrace.repository.ApplicationStatusRepository;
import com.jobtrace.repository.JobPostingRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@ActiveProfiles("local")
@Transactional
class JobPostingNPlusOneTest {

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Autowired
    private ApplicationStatusRepository applicationStatusRepository;

    @Autowired
    private com.jobtrace.repository.UserRepository userRepository;

    @PersistenceContext
    private EntityManager em;

    private Statistics statistics;
    private User testUser;
    private static final int DATA_SIZE = 500;

    @BeforeEach
    @Transactional
    void setUp() {
        org.hibernate.SessionFactory sessionFactory =
                em.getEntityManagerFactory().unwrap(org.hibernate.SessionFactory.class);
        statistics = sessionFactory.getStatistics();
        statistics.setStatisticsEnabled(true);
        statistics.clear();

        testUser = userRepository.save(User.builder()
                .email("test@jobtrace.com")
                .name("테스트유저")
                .password("dummy")
                .build());

        for (int i = 0; i < DATA_SIZE; i++) {
            JobPosting posting = JobPosting.builder()
                    .user(testUser)
                    .companyName("회사_" + i)
                    .role("백엔드 개발자")
                    .jobUrl("https://example.com/job/" + i)
                    .platform("jobtrace")
                    .startDate(LocalDate.now())
                    .deadline(LocalDate.now().plusDays(14))
                    .build();
            JobPosting saved = jobPostingRepository.save(posting);

            ApplicationStatus status = ApplicationStatus.builder()
                    .jobPosting(saved)
                    .status(StatusType.BOOKMARKED)
                    .changedAt(LocalDateTime.now())
                    .build();
            applicationStatusRepository.save(status);
        }

        em.flush();
        em.clear();
        statistics.clear();
    }

    @Test
    void 기존_방식_N플러스1_문제_측정() {
        long start = System.nanoTime();

        List<JobPosting> postings = jobPostingRepository.findAllByUser(testUser);
        for (JobPosting posting : postings) {
            applicationStatusRepository
                    .findTopByJobPostingOrderByChangedAtDesc(posting);
        }

        long elapsedMs = (System.nanoTime() - start) / 1_000_000;
        long queryCount = statistics.getPrepareStatementCount();

        System.out.println("===== [개선 전: N+1 방식] =====");
        System.out.println("데이터 건수: " + DATA_SIZE);
        System.out.println("실행된 쿼리 수: " + queryCount);
        System.out.println("소요 시간(ms): " + elapsedMs);
    }

    @Test
    void 개선_방식_fetch_join_측정() {
        long start = System.nanoTime();

        List<JobPosting> postings =
                jobPostingRepository.findAllByUserWithStatuses(testUser);
        for (JobPosting posting : postings) {
            posting.getApplicationStatuses().size();
        }

        long elapsedMs = (System.nanoTime() - start) / 1_000_000;
        long queryCount = statistics.getPrepareStatementCount();

        System.out.println("===== [개선 후: fetch join 방식] =====");
        System.out.println("데이터 건수: " + DATA_SIZE);
        System.out.println("실행된 쿼리 수: " + queryCount);
        System.out.println("소요 시간(ms): " + elapsedMs);
    }
}