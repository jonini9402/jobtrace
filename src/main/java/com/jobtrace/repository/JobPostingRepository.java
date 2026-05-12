package com.jobtrace.repository;

import com.jobtrace.domain.JobPosting;
import com.jobtrace.domain.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
    List<JobPosting> findAllByUser(User user);

    //월별 지원수 전체 달 집계 쿼리
    @Query("SELECT FUNCTION('DATE_FORMAT', j.createdAt, '%Y-%m'), COUNT(j) " +
            "FROM JobPosting j " +
            "WHERE j.user = :user " +
            "GROUP BY FUNCTION('DATE_FORMAT', j.createdAt, '%Y-%m') " +
            "ORDER BY 1")
    List<Object[]> countMonthly(@Param("user") User user);

}
