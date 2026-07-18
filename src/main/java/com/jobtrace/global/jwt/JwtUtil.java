package com.jobtrace.global.jwt;



import com.jobtrace.global.exception.CustomException;
import com.jobtrace.global.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final long TOKEN_TIME = 60 * 60 * 1000L; //60분

    @Value("${jwt.secret}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    //키를 초기화
    @PostConstruct
    public void init() {
        byte[] bytes = secretKey.getBytes();
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createToken(long userId, String email) {
        Date date = new Date();
        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(String.valueOf(userId))
                        .claim("email", email)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    public String subStringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        log.error("Token Not Found");
        throw new CustomException(ErrorCode.TOKEN_NOT_FOUND);
    }

    //jwt의 페이로드 안에 정보들이 들어있는데 이 정보 하나하나를 Claim 이라고 함
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            log.error("Invaild JWT token: {}", e.getMessage());
            return false;
        }
    }

    //토큰에서 이메일 꺼내기
    public String getEmail(String token) {
        return extractClaims(token).get("email", String.class);
    }

    //토큰 남은 만료시간 계산
    public long getRemainingTime(String token) {
        //만료시간
        long expirationTime = extractClaims(token).getExpiration().getTime();
        //지금 시간
        long time = new Date().getTime();

        //남은 만료 시간 = 만료시간 - 지금 시간
        long remainingTime = expirationTime - time;

        return remainingTime;
    }
}



