package com.jobtrace.global.jwt;

import com.jobtrace.domain.User;
import com.jobtrace.global.exception.CustomException;
import com.jobtrace.global.exception.ErrorCode;
import com.jobtrace.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    //1. 헤더에서 토큰 꺼내기    
    String token = resolveToken(request);

        //2. 토큰 유효성 검증
        if(token != null && jwtUtil.validateToken(token)){

            //3. 토큰에서 이메일 꺼내기
            String email = jwtUtil.getEmail(token);

            //4. DB에서 User 조회
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

            //SecurityContext에 인증정보(user 객체) 저장
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user,
                    null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        //다음 필터로
        filterChain.doFilter(request, response);
    }
    
    private String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
        return bearerToken.substring(7);
    }
    return null;
    }
    

}
