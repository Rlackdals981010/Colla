package com.dolloer.colla.response.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiResponseScheduleEnum implements ApiResponseEnum {
    // 200
    SCHEDULE_CREATE_SUCCESS(HttpStatus.OK,"스케줄 생성에 성공하였습니다."),
    SCHEDULE_GET_LIST_SUCCESS(HttpStatus.OK,"스케줄 전체 조회에 성공하였습니다."),

    // 400
    INVALID_SCHEDULE_DATE(HttpStatus.BAD_REQUEST, "시작일은 종료일보다 앞서야 합니다."),

    //401
    MEMBER_WRONG_PASSWORD(HttpStatus.UNAUTHORIZED,"비밀번호가 틀렸습니다."),

    // 403
    NOT_PROJECT_MEMBER(HttpStatus.FORBIDDEN,"존재하지 않는 맴버 입니다."),


    //404
    LINK_NOT_EXIST(HttpStatus.NOT_FOUND,"존재하지 않는 링크 글 입니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    ApiResponseScheduleEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
        code = httpStatus.value();
    }
}
