package com.jobtrace.auth.service;

import com.jobtrace.auth.dto.request.LoginRequest;
import com.jobtrace.auth.dto.request.SignUpRequest;
import com.jobtrace.auth.dto.response.LoginResponse;
import com.jobtrace.auth.dto.response.SignUpResponse;

public interface AuthService {

    //회원가입
    SignUpResponse signUp(SignUpRequest request);

    //로그인
    LoginResponse login(LoginRequest request);

    //회원탈퇴
 //   void withdraw(Long userId);

    //JWT 토큰 삭제 방식으로 로그아웃
}
