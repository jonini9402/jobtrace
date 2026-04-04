package com.jobtrace.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //enum
    DUPLICATE_EMAIL("USER_001", "이미 사용중인 이메일"),
    USER_NOT_FOUND("USER_002", "유저를 찾을 수 없음");

    private String code;
    private String message;
}
