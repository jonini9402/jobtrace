package com.jobtrace.repository;

import com.jobtrace.domain.ApplicationStatus;
import com.jobtrace.domain.JobPosting;
import com.jobtrace.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApplicationStatusRepository extends JpaRepository<ApplicationStatus, Long> {

    Optional<ApplicationStatus> findTopByJobPostingOrderByChangedAtDesc(JobPosting jobPosting);

    @Query("SELECT a.status, COUNT(a) FROM ApplicationStatus a " +
            "WHERE a.jobPosting.user = :user " +
            "AND a.changedAt = (" +
            "  SELECT MAX(a2.changedAt) FROM ApplicationStatus a2 " +
            "  WHERE a2.jobPosting = a.jobPosting" +
            ") " +
            "GROUP BY a.status")
    List<Object[]> countByStatusLatest(@Param("user") User user);

    @Query("SELECT COUNT(DISTINCT a.jobPosting) FROM ApplicationStatus a " +
    "WHERE a.jobPosting.user = :user" +
    "AND a.status != '관심공고' " +
    "AND a.changedAt = (" +
    " SELECT MAX(a2.changedAt) FROM ApplicationStatus a2 " +
    " WHERE a2.jobPosting = a.jobPosting" + ")")
    long countApplied(@Param("user") User user);
}
