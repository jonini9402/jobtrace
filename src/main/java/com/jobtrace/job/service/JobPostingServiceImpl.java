package com.jobtrace.job.service;

import com.jobtrace.domain.JobPosting;
import com.jobtrace.domain.User;
import com.jobtrace.job.dto.request.JobPostingRequest;
import com.jobtrace.job.dto.response.JobPostingResponse;
import com.jobtrace.repository.JobPostingRepository;
import com.jobtrace.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

        //JobPostingResponse의 entity의 빌더 객체 반환
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


    public List<JobPostingResponse> getMyPosts(User user){
        return jobPostingRepository.findAllByUser(user)
                .stream()// stream 체인 열어주기
                .map(jobPosting -> JobPostingResponse.builder()
                    .id(jobPosting.getId())
                    .userId(user.getId())
                    .companyName(jobPosting.getCompanyName())
                    .role(jobPosting.getRole())
                    .jobUrl(jobPosting.getJobUrl())
                    .platform(jobPosting.getPlatform())
                    .startDate(jobPosting.getStartDate())
                    .deadline(jobPosting.getDeadline())
                    .build())
                .collect(Collectors.toList()); // stream 체인 마무리

    }



    }



