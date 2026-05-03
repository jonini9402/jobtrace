package com.jobtrace.repository;

import com.jobtrace.domain.ApplicationStatus;
import com.jobtrace.domain.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplicationStatusRepository extends JpaRepository<ApplicationStatus, Long> {

    Optional<ApplicationStatus> findTopByJobPostingOrderByChangedAtDesc(JobPosting jobPosting);

}
