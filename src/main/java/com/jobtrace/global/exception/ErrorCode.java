package com.jobtrace.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //enum
    DUPLICATE_EMAIL("USER_001", "이미 사용 중인 이메일입니다"),
    USER_NOT_FOUND("USER_002", "존재하지 않는 계정입니다"),
    INVALID_PASSWORD("USER_003", "이메일 또는 비밀번호를 확인해주세요"),
    TOKEN_NOT_FOUND("AUTH_001", "로그인이 필요합니다");

    private String code;
    private String message;
}
