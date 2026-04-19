package com.jobtrace.job.service;

import com.jobtrace.domain.JobPosting;
import com.jobtrace.domain.User;
import com.jobtrace.job.dto.request.JobPostingRequest;
import com.jobtrace.job.dto.response.JobPostingResponse;
import com.jobtrace.repository.JobPostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobPostingServiceImpl implements JobPostingService{

    private final JobPostingRepository jobPostingRepository;

    @Override
    @Transactional
    public JobPostingResponse createPost(JobPostingRequest request, User user){

        //entity 객체 생성 (user 객체도 여기에 넣어준다)
        JobPosting jobPosting = JobPosting.builder()
                .companyName(request.getCompanyName())
                .role(request.getRole())
                .jobUrl(request.getJobUrl())
                .platform(request.getPlatform())
                .startDate(request.getStartDate())
                .deadline(request.getDeadline())
                .user(user)
                .build();

        //entity 객체를 레포지토리에 저장
        jobPostingRepository.save(jobPosting);

        //JobPostingResponse의 빌더 객체 반환
        return JobPostingResponse.builder()
                .id(jobPosting.getId())
                .userId(user.getId())
                .companyName(jobPosting.getCompanyName())
                .role(jobPosting.getRole())
                .jobUrl(jobPosting.getJobUrl())
                .platform(jobPosting.getPlatform())
                .startDate(jobPosting.getStartDate())
                .deadline(jobPosting.getDeadline())
                .build();
    }


}

