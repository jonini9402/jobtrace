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

    //공고 목록 조회시 최신상태 조회
    Optional<ApplicationStatus> findTopByJobPostingOrderByChangedAtDesc(JobPosting jobPosting);

    //전체 공고의 상태 개수 반환
    @Query("SELECT a.status, COUNT(a) FROM ApplicationStatus a " +
            " WHERE a.jobPosting.user = :user " +
            "AND a.changedAt = (SELECT MAX(a2.changedAt) FROM ApplicationStatus a2 " +
            "WHERE a2.jobPosting = a.jobPosting) " +
            "GROUP BY a.status")
    List<Object[]> countByStatusLatest(@Param("user") User user);

    //실제지원수
    @Query("SELECT COUNT(DISTINCT a.jobPosting) FROM ApplicationStatus a " +
            " WHERE a.jobPosting.user = :user" +
            " AND a.status != :bookmarked " +
            " AND a.changedAt = (" +
            " SELECT MAX(a2.changedAt) FROM ApplicationStatus a2 " +
            " WHERE a2.jobPosting = a.jobPosting" + ")")
    long countApplied(@Param("user") User user, @Param("bookmarked") String bookmarked);


    //단계별 집계
    @Query("SELECT COUNT(DISTINCT a.jobPosting) FROM ApplicationStatus a " +
            " WHERE a.jobPosting.user = :user" +
            " AND a.status IN (:statuses)" +
            " AND a.changedAt = (" +
            " SELECT MAX(a2.changedAt) FROM ApplicationStatus a2 " +
            " WHERE a2.jobPosting = a.jobPosting) ")
    long countByLatestStatusIn(@Param("user") User user, @Param("statuses") List<String> statuses);




}