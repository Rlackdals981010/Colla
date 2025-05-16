package com.dolloer.colla.response.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiResponseAuthEnum implements ApiResponseEnum {
    // 200
    MEMBER_CREATE_SUCCESS(HttpStatus.OK,"회원가입에 성공하였습니다."),

    // 400
    MEMBER_ALREADY_EXIST(HttpStatus.BAD_REQUEST,"이미 회원가입된 이메일 입니다.");


    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    ApiResponseAuthEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
        code = httpStatus.value();
    }
}
