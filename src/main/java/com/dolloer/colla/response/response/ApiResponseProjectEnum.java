package com.dolloer.colla.response.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiResponseProjectEnum implements ApiResponseEnum {
    // 200
    PROJECT_CREATE_SUCCESS(HttpStatus.OK,"프로젝트 생성에 성공하였습니다."),
    PROJECT_LIST_GET_SUCCESS(HttpStatus.OK,"프로젝트 목록 조회에 성공하였습니다."),
    PROJECT_INVITE_SUCCESS(HttpStatus.OK,"프로젝트 초대에 성공하였습니다."),


    // 400
    MEMBER_ALREADY_EXIST(HttpStatus.BAD_REQUEST,"이미 회원가입된 회원 입니다."),
    MEMBER_NOT_EXIST(HttpStatus.BAD_REQUEST,"본 서비스 회원이 아닙니다."),
    PROJECT_MEMBER_NOT_EXIST(HttpStatus.BAD_REQUEST,"속한 프로젝트가 없습니다."),

    //401
    MEMBER_WRONG_PASSWORD(HttpStatus.UNAUTHORIZED,"비밀번호가 틀렸습니다."),

    // 403
    NOT_PROJECT_MEMBER(HttpStatus.FORBIDDEN,"존재하지 않는 맴버 입니다."),
    NOT_ENOUGH_PERMISSION(HttpStatus.FORBIDDEN,"초대가능한 권한이 아닙니다."),
    NOT_MATCHING_USER(HttpStatus.FORBIDDEN,"해당 멤버가 아닙니다."),
    NOT_INVITED_MEMBER(HttpStatus.FORBIDDEN,"초대한 멤버가 아닙니다."),
    ALREADY_RESPONDED(HttpStatus.FORBIDDEN,"이미 응답한 상태입니다."),

    //404
    PROJECT_NOT_EXIST(HttpStatus.NOT_FOUND,"존재하지 않는 프로젝트 입니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    ApiResponseProjectEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
        code = httpStatus.value();
    }
}
