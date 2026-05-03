package com.jobtrace.job.service;

import com.jobtrace.domain.JobPosting;
import com.jobtrace.domain.User;
import com.jobtrace.job.dto.request.ApplicationStatusRequest;
import com.jobtrace.job.dto.request.JobPostingRequest;
import com.jobtrace.job.dto.request.MemoUpdateRequest;
import com.jobtrace.job.dto.response.ApplicationStatusResponse;
import com.jobtrace.job.dto.response.JobPostingResponse;

import java.util.List;

public interface JobPostingService {
    JobPostingResponse createPost(JobPostingRequest request, User user);

    List<JobPostingResponse> getMyPosts(User user);

    JobPostingResponse getPostDetail(Long id, User user);

    JobPostingResponse updatePost(Long id,User user, JobPostingRequest request);

    void deletePost(Long id, User user);

    ApplicationStatusResponse updateStatus(Long id, User user, ApplicationStatusRequest request);

    JobPostingResponse updateMemo(Long id, User user, MemoUpdateRequest request);
}


