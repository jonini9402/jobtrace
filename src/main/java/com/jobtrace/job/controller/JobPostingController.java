package com.jobtrace.job.controller;

import com.jobtrace.domain.User;
import com.jobtrace.job.dto.request.JobPostingRequest;
import com.jobtrace.job.dto.response.JobPostingResponse;
import com.jobtrace.job.service.JobPostingService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/jobs")
public class JobPostingController {

    private final JobPostingService jobPostingService;

    @PostMapping("/create")
    public ResponseEntity<JobPostingResponse> jobPost(@RequestBody JobPostingRequest jobPostingRequest,
                                                      @AuthenticationPrincipal User user){

        //서비스가 갖고 있는 createPost 메서드에 요청을 파라미터로 넣고 메서드 호출해
        //그래서 응답 객체 만들고 거기에 할당해
        JobPostingResponse response = jobPostingService.createPost(jobPostingRequest, user);

        //응답객체를 바디로 넣고 ResponseEntity.응답 코드 반환해
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



}
