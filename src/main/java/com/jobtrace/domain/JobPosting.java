package com.jobtrace.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_posting")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JobPosting extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String companyName;

    private String role;

    private String jobUrl;

    private String platform;

    private LocalDate startDate;

    private LocalDate deadLine;

    private String memo;







}
