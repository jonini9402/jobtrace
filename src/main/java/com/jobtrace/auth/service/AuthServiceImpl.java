package com.jobtrace.auth.service;

import com.jobtrace.auth.dto.request.LoginRequest;
import com.jobtrace.auth.dto.request.SignUpRequest;
import com.jobtrace.auth.dto.response.LoginResponse;
import com.jobtrace.auth.dto.response.SignUpResponse;
import com.jobtrace.domain.User;
import com.jobtrace.global.exception.CustomException;
import com.jobtrace.global.exception.ErrorCode;
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

        //signUpResponce 반환
        return SignUpResponse.builder()
                .email(user.getEmail())
                .name(user.getName())
                .build();

    }

//    @Override
//    @Transactional
//    public LoginResponse login(LoginRequest request){
//        //요청에 있는 이메일이랑 비번 변수에 저장하기
//        String email = request.getEmail();
//        String password = request.getPassword();
//
//        //db에 있는 이메일이랑 비번 가져오기
//        userRepository.findByEmail(email);
//
//
//
//        //둘이 일치하는지 확인
//
//        //일치하면 응답 객체 생성해서 반환해주기
//
//        //일치하지 않으면 에러 던지기
//
//    }


//    @Override
//    public void withdraw(Long userId){
//
//
//    }
}

