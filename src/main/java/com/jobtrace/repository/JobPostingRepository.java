package com.jobtrace.repository;

import com.jobtrace.domain.JobPosting;
import com.jobtrace.domain.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
    List<JobPosting> findAllByUser(User user);



}
