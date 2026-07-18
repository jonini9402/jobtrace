package com.jobtrace.auth.controller;

import com.jobtrace.auth.dto.request.LoginRequest;
import com.jobtrace.auth.dto.request.SignUpRequest;
import com.jobtrace.auth.dto.response.LoginResponse;
import com.jobtrace.auth.dto.response.SignUpResponse;
import com.jobtrace.auth.service.AuthService;
import com.jobtrace.global.exception.CustomException;
import com.jobtrace.global.exception.ErrorCode;
import com.jobtrace.global.jwt.JwtUtil;
import com.jobtrace.global.redis.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final TokenBlacklistService tokenBlacklistService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signUp (@RequestBody SignUpRequest signUpRequest){
        SignUpResponse signUpResponse = authService.signUp(signUpRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(signUpResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login (@RequestBody LoginRequest loginRequest){
        LoginResponse loginResponse = authService.login(loginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout (HttpServletRequest request){
        //토큰 꺼내기
        String token = request.getHeader("Authorization").substring(7);

        //blacklist에 넣기
        tokenBlacklistService.addBlacklist(token, jwtUtil.getRemainingTime(token));

        //응답 반환
        return ResponseEntity.ok("LOGOUT_SUCCESS");
    }

}
