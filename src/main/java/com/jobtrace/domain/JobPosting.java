package com.jobtrace.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_posting")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobPosting extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "company_name")
    private String companyName;

    private String role;

    @Column(name = "job_url")
    private String jobUrl;

    private String platform;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "dead_line")
    private LocalDate deadline;

    private String memo;







}
