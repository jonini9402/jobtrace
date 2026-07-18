package com.jobtrace.global.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

    private final RedisTemplate<String, String> redisTemplate;

    // 토큰을 블랙리스트에 추가
    public void addBlacklist(String token, long remainingTime) {

        redisTemplate.opsForValue().set(token, "logout", remainingTime, TimeUnit.MILLISECONDS);

    }

    // 토큰이 블랙리스트에 있는지 확인
    public boolean isBlacklisted(String token) {
        // Redis에서 key로 조회했을 때 null이 아니면 블랙리스트에 있는 것
        if(redisTemplate.opsForValue().get(token) != null){
            System.out.println("로그아웃된 토큰 입니다.");
            return true;
        }
        return false;

    }


}
