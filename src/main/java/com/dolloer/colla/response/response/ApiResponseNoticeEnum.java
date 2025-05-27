package com.dolloer.colla.response.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiResponseNoticeEnum implements ApiResponseEnum {
    // 200
    NOTICE_CREATE_SUCCESS(HttpStatus.OK,"공지 생성에 성공하였습니다."),
    NOTICE_LIST_READ_SUCCESS(HttpStatus.OK,"공지 전체 조회에 성공하였습니다."),
    NOTICE_READ_SUCCESS(HttpStatus.OK,"단일 공지 조회에 성공하였습니다."),
    NOTICE_UPDATE_SUCCESS(HttpStatus.OK,"공지 수정에 성공하였습니다."),
    NOTICE_DELETE_SUCCESS(HttpStatus.OK,"공지 삭제에 성공하였습니다."),
    NOTICE_LIST_SEARCH_SUCCESS(HttpStatus.OK,"공지 검색에 성공하였습니다."),

    // 400
    DUPLICATED_NOTICE(HttpStatus.BAD_REQUEST,"동일한 공지명이 존재합니다."),

    //401
    MEMBER_WRONG_PASSWORD(HttpStatus.UNAUTHORIZED,"비밀번호가 틀렸습니다."),

    // 403
    NOT_THIS_PROJECT_NOTICE(HttpStatus.FORBIDDEN,"본 프로젝트의 공지가 아닙니다."),
    NOTICE_NOT_EXIST(HttpStatus.FORBIDDEN,"존재하지 않는 맴버 입니다."),
    NOT_ENOUGH_PERMISSION(HttpStatus.FORBIDDEN,"해당 기능을 사용할 수 있는 권한이 아닙니다."),

    //404
    LINK_NOT_EXIST(HttpStatus.NOT_FOUND,"존재하지 않는 링크 글 입니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    ApiResponseNoticeEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
        code = httpStatus.value();
    }
}
