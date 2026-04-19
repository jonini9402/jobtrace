package com.jobtrace.job.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobPostingResponse {
    private Long id;
    private Long userId;
    private String companyName;
    private String role;
    private String jobUrl;
    private String platform;
    private LocalDate deadline;
    private LocalDate startDate;
}
