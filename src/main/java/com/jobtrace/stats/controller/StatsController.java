package com.jobtrace.stats.controller;

import com.jobtrace.domain.User;
import com.jobtrace.stats.dto.StatsResponse;
import com.jobtrace.stats.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stats")
public class StatsController {
    private final StatsService statsService;

    @GetMapping
    public ResponseEntity<StatsResponse> getStats(@AuthenticationPrincipal User user){
        StatsResponse response = statsService.getStats(user);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

}
