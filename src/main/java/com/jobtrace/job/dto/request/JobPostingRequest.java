package com.jobtrace.job.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobPostingRequest {
    private String companyName;
    private String role;
    private String jobUrl;
    private String platform;
    private LocalDate deadline;
    private LocalDate startDate;
}
