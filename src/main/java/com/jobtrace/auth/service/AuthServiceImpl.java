package com.jobtrace.auth.service;

import com.jobtrace.auth.dto.request.LoginRequest;
import com.jobtrace.auth.dto.request.SignUpRequest;
import com.jobtrace.auth.dto.response.LoginResponse;
import com.jobtrace.auth.dto.response.SignUpResponse;
import com.jobtrace.domain.User;
import com.jobtrace.global.exception.CustomException;
import com.jobtrace.global.exception.ErrorCode;
import com.jobtrace.global.jwt.JwtUtil;
import com.jobtrace.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public SignUpResponse signUp(SignUpRequest request) {
        //이메일 중복체크
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);

        }
        //비밀번호 암호화
        String rawPassword = request.getPassword();
        String password = passwordEncoder.encode(rawPassword);

        //User Entity 생성 (Builder)
        User user = User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .password(password)
                .build();

        //DB에 저장
        userRepository.save(user);

        //signUpResponce 의 빌더 객체 반환
        return SignUpResponse.builder()
                .email(user.getEmail())
                .name(user.getName())
                .build();

    }

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request){
        //이메일로 유저 찾기
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        //비밀번호로 일치하는지 확인
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        //일치하면 JWT 토큰 생성
        String token = jwtUtil.createToken(user.getId(), user.getEmail());

        //응답의 빌더 객체 반환
         return LoginResponse.builder()
                .accessToken(token)
                .name(user.getName())
                .build();


    }


//    @Override
//    public void withdraw(Long userId){
//
//
//    }
}

