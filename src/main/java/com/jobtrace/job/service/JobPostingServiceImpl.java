package com.jobtrace.job.service;

import com.jobtrace.domain.ApplicationStatus;
import com.jobtrace.domain.JobPosting;
import com.jobtrace.domain.User;
import com.jobtrace.global.common.StatusType;
import com.jobtrace.global.exception.CustomException;
import com.jobtrace.global.exception.ErrorCode;
import com.jobtrace.job.dto.request.ApplicationStatusRequest;
import com.jobtrace.job.dto.request.JobPostingRequest;
import com.jobtrace.job.dto.request.MemoUpdateRequest;
import com.jobtrace.job.dto.response.ApplicationStatusResponse;
import com.jobtrace.job.dto.response.JobPostingResponse;
import com.jobtrace.repository.ApplicationStatusRepository;
import com.jobtrace.repository.JobPostingRepository;
import jakarta.websocket.OnError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobPostingServiceImpl implements JobPostingService{

    private final JobPostingRepository jobPostingRepository;
    private final ApplicationStatusRepository applicationStatusRepository;

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
        JobPosting savedJobPosting =  jobPostingRepository.save(jobPosting);

        //최초로 등록 시, BOOKMARKED 상태로 저장
        ApplicationStatus initialStatus = ApplicationStatus.builder()
                .jobPosting(savedJobPosting)
                .status(StatusType.BOOKMARKED)
                .changedAt(LocalDateTime.now())
                .build();
        applicationStatusRepository.save(initialStatus);

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
                .status(initialStatus.getStatus())
                .build();
    }


    public List<JobPostingResponse> getMyPosts(User user){

        return jobPostingRepository.findAllByUserWithStatuses(user)
                // 1. 내 공고 전체를 List<JobPosting>으로 가져옴
                .stream() //2. 리스트를 하나씩 꺼낼 준비
                .map(jobPosting -> {
                    // fetch join으로 이미 로딩된 컬렉션에서 최신 상태 꺼냄 (추가 쿼리 없음)
                    String status = jobPosting.getApplicationStatuses().stream()
                            .max(Comparator.comparing(ApplicationStatus::getChangedAt))
                            .map(ApplicationStatus::getStatus)
                            .orElse("미지원");

                //5. 조회한 status 포함해서 Response 조립
                return JobPostingResponse.builder()
                    .id(jobPosting.getId())
                    .userId(user.getId())
                    .companyName(jobPosting.getCompanyName())
                    .role(jobPosting.getRole())
                    .jobUrl(jobPosting.getJobUrl())
                    .platform(jobPosting.getPlatform())
                    .startDate(jobPosting.getStartDate())
                    .deadline(jobPosting.getDeadline())
                        .status(status)
                    .build();
                })
                .collect(Collectors.toList()); // 6. 변환된 것들을 다시 List로 모음
    }
    //단일 조회도 entity 객체 자체가 아닌 응답 dto로 포장해서 반환해야됨 !!
    public JobPostingResponse getPostDetail(Long id, User user) {


        JobPosting jobPosting = jobPostingRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));


        //파라미터로 온 userId와 jobPosting의 userId가 동일한지 검증
        if (!jobPosting.getUser().getId().equals(user.getId())){
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        //상태 확인
        String status = applicationStatusRepository
                .findTopByJobPostingOrderByChangedAtDesc(jobPosting)
                .map(ApplicationStatus::getStatus)
                .orElse("미지원");  // 상태 이력 없으면 기본값

        return JobPostingResponse.builder()
                .id(jobPosting.getId())
                .status(status)
                .userId(user.getId())
                .companyName(jobPosting.getCompanyName())
                .role(jobPosting.getRole())
                .jobUrl(jobPosting.getJobUrl())
                .platform(jobPosting.getPlatform())
                .startDate(jobPosting.getStartDate())
                .deadline(jobPosting.getDeadline())
                .memo(jobPosting.getMemo())
                .build();

    }
    @Override
    @Transactional
    public JobPostingResponse updatePost(Long id, User user, JobPostingRequest request){

        //JobPosting jobPosting = repository.findById(id) 를 통해서 해당하는 포스트 객체 가져와줌
        JobPosting jobPosting = jobPostingRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        //userId가 맞는지 검증 로직
        if(!jobPosting.getUser().getId().equals(user.getId())){
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        //요청에서 필드 값이 null이면 기존 필드값을 유지하고, 값이 있으면 새로운 값으로 교체하기
        if(request.getCompanyName() != null) jobPosting.setCompanyName(request.getCompanyName());
        if(request.getRole() != null) jobPosting.setRole(request.getRole());
        System.out.println("role 변경됨: " + jobPosting.getRole());
        if(request.getJobUrl() != null) jobPosting.setJobUrl(request.getJobUrl());
        if(request.getPlatform() != null) jobPosting.setPlatform(request.getPlatform());
        if(request.getStartDate() != null) jobPosting.setStartDate(request.getStartDate());
        if(request.getDeadline() != null) jobPosting.setDeadline(request.getDeadline());


        jobPostingRepository.save(jobPosting);


        //요청dto에서 값이 들어가있는 것을 빌더를 통해 리턴하기
        return JobPostingResponse.builder()
                .id(jobPosting.getId())
                .userId(jobPosting.getUser().getId())
                .companyName(jobPosting.getCompanyName())
                .role(jobPosting.getRole())
                .jobUrl(jobPosting.getJobUrl())
                .platform(jobPosting.getPlatform())
                .startDate(jobPosting.getStartDate())
                .deadline(jobPosting.getDeadline())
                .build();

    }
    @Override
    @Transactional
    public void deletePost(Long id,User user){

        //id로 게시물 찾기
        JobPosting jobPosting = jobPostingRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        //id가 불일치하면 에러
        if(!jobPosting.getUser().getId().equals(user.getId())){
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        //jobPosting을 db에서 삭제
        jobPostingRepository.deleteById(id);
    }

    @Override
    @Transactional
    public ApplicationStatusResponse updateStatus(Long id, User user, ApplicationStatusRequest request){
        //id로 게시물 찾기
        JobPosting jobPosting = jobPostingRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        //id가 불일치 하면 에러
        if(!jobPosting.getUser().getId().equals(user.getId())){
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        //새 상태 이력 생성
        ApplicationStatus applicationStatus = ApplicationStatus.builder()
                .jobPosting(jobPosting)
                .status(request.getStatus())
                .changedAt(LocalDateTime.now())
                .build();

        //저장객체 생성
        ApplicationStatus saved = applicationStatusRepository.save(applicationStatus);

        return ApplicationStatusResponse.builder()
                .id(saved.getId())
                .jobPostingId(jobPosting.getId())
                .status(saved.getStatus())
                .changeAt(saved.getChangedAt())
                .build();
    }

    @Override
    @Transactional
    public JobPostingResponse updateMemo(Long id, User user, MemoUpdateRequest request){
        //id로 포스트 찾기
        JobPosting jobPosting = jobPostingRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        //권한 확인
        if(!jobPosting.getUser().getId().equals(user.getId())){
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        jobPosting.setMemo(request.getMemo());

        jobPostingRepository.save(jobPosting);

        return JobPostingResponse.builder()
                .id(jobPosting.getId())
                .userId(jobPosting.getUser().getId())
                .companyName(jobPosting.getCompanyName())
                .role(jobPosting.getRole())
                .jobUrl(jobPosting.getJobUrl())
                .platform(jobPosting.getPlatform())
                .startDate(jobPosting.getStartDate())
                .deadline(jobPosting.getDeadline())
                .memo(jobPosting.getMemo())
                .build();

    }
    }



