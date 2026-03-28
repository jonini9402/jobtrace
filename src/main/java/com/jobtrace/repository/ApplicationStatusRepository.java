package com.jobtrace.repository;

import com.jobtrace.domain.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationStatusRepository extends JpaRepository<ApplicationStatus, Long> {
}
