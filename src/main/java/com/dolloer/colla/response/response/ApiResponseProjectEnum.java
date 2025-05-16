package com.dolloer.colla.response.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiResponseProjectEnum implements ApiResponseEnum {
    // 200
    PROJECT_CREATE_SUCCESS(HttpStatus.OK,"프로젝트 생성에 성공하였습니다."),


    // 400

    MEMBER_ALREADY_EXIST(HttpStatus.BAD_REQUEST,"이미 회원가입된 회원 입니다."),

    //401
    MEMBER_WRONG_PASSWORD(HttpStatus.UNAUTHORIZED,"비밀번호가 틀렸습니다.");


    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    ApiResponseProjectEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
        code = httpStatus.value();
    }
}
