package com.jobtrace.job.service;

import com.jobtrace.domain.User;
import com.jobtrace.job.dto.request.JobPostingRequest;
import com.jobtrace.job.dto.response.JobPostingResponse;

public interface JobPostingService {
    JobPostingResponse createPost(JobPostingRequest request, User user);
}
