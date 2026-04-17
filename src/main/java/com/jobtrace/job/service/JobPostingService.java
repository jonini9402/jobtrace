package com.jobtrace.job.service;

import com.jobtrace.domain.JobPosting;
import com.jobtrace.domain.User;
import com.jobtrace.job.dto.request.JobPostingRequest;
import com.jobtrace.job.dto.response.JobPostingResponse;

import java.util.List;

public interface JobPostingService {
    JobPostingResponse createPost(JobPostingRequest request, User user);

    List<JobPostingResponse> getMyPosts(User user);

    JobPostingResponse getPostDetail(Long id, User user);
}


