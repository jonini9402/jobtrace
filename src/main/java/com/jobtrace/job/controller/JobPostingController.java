package com.jobtrace.job.controller;

import com.jobtrace.domain.User;
import com.jobtrace.job.dto.request.ApplicationStatusRequest;
import com.jobtrace.job.dto.request.JobPostingRequest;
import com.jobtrace.job.dto.request.MemoUpdateRequest;
import com.jobtrace.job.dto.response.ApplicationStatusResponse;
import com.jobtrace.job.dto.response.JobPostingResponse;
import com.jobtrace.job.service.JobPostingService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/jobs")
public class JobPostingController {

    private final JobPostingService jobPostingService;

    @PostMapping
    public ResponseEntity<JobPostingResponse> jobPost(@RequestBody JobPostingRequest jobPostingRequest,
                                                      @AuthenticationPrincipal User user) {

        //서비스가 갖고 있는 createPost 메서드에 요청을 파라미터로 넣고 메서드 호출해
        //그래서 응답 객체 만들고 거기에 할당해
        JobPostingResponse response = jobPostingService.createPost(jobPostingRequest, user);

        //응답객체를 바디로 넣고 ResponseEntity.응답 코드 반환해
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<JobPostingResponse>> getPost(@AuthenticationPrincipal User user) {
        List<JobPostingResponse> responses = jobPostingService.getMyPosts(user);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobPostingResponse> getPostDetail(@PathVariable Long id,
                                                            @AuthenticationPrincipal User user) {
        JobPostingResponse response = jobPostingService.getPostDetail(id, user);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobPostingResponse> updatePost(@PathVariable Long id, @AuthenticationPrincipal User user,
                                                         @RequestBody JobPostingRequest request) {
        JobPostingResponse response = jobPostingService.updatePost(id, user, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);


}

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id, @AuthenticationPrincipal User user) {
        jobPostingService.deletePost(id, user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<ApplicationStatusResponse> updateStatus(@PathVariable Long id, @AuthenticationPrincipal User user, @RequestBody ApplicationStatusRequest request){
        ApplicationStatusResponse response = jobPostingService.updateStatus(id, user, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @PostMapping("/{id}/memo")
    public ResponseEntity<JobPostingResponse> updateMemo(@PathVariable Long id, @AuthenticationPrincipal User user, @RequestBody MemoUpdateRequest request){
        JobPostingResponse response = jobPostingService.updateMemo(id, user, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
