package com.jobtrace.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_posting")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JobPosting extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "jobPosting", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<ApplicationStatus> applicationStatuses;

    @Column(name = "company_name")
    private String companyName;

    private String role;

    @Column(name = "job_url")
    private String jobUrl;

    private String platform;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "dead_line", nullable = true)
    private LocalDate deadline;

    private String memo;

    @Column(name = "experience_level")
    private String experienceLevel;
}
