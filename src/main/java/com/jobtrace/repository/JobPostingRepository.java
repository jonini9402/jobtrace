package com.jobtrace.repository;

import com.jobtrace.domain.JobPosting;
import com.jobtrace.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
    List<JobPosting> findAllByUser(User user);
}
