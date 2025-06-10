package com.dolloer.colla.response.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiResponseScheduleEnum implements ApiResponseEnum {
    // 200
    SCHEDULE_CREATE_SUCCESS(HttpStatus.OK,"스케줄 생성에 성공하였습니다."),
    SCHEDULE_GET_LIST_SUCCESS(HttpStatus.OK,"스케줄 전체 조회에 성공하였습니다."),
    SCHEDULE_GET_LIST_AT_SUCCESS(HttpStatus.OK,"특정 일자의 스케줄 전체 조회에 성공하였습니다."),
    SCHEDULE_UPDATE_SUCCESS(HttpStatus.OK,"스케줄 수정에 성공하였습니다."),
    SCHEDULE_PROCESS_UPDATE_SUCCESS(HttpStatus.OK,"진행률 수정에 성공하였습니다."),
    SCHEDULE_DELETE_SUCCESS(HttpStatus.OK,"스케줄 삭제에 성공하였습니다."),

    // 400
    INVALID_SCHEDULE_DATE(HttpStatus.BAD_REQUEST, "시작일은 종료일보다 앞서야 합니다."),

    //401
    MEMBER_WRONG_PASSWORD(HttpStatus.UNAUTHORIZED,"비밀번호가 틀렸습니다."),

    // 403
    SCHEDULE_NOT_EXIST(HttpStatus.FORBIDDEN,"일정이 존재하지 않습니다."),
    SCHEDULE_PROJECT_DOESNT_MATCH(HttpStatus.FORBIDDEN,"본 프로젝트의 일정이 아닙니다."),
    NOT_SCHEDULE_MANAGER(HttpStatus.FORBIDDEN,"담당자가 아닙니다."),



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
