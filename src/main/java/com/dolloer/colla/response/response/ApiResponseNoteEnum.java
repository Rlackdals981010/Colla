package com.dolloer.colla.response.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiResponseNoteEnum implements ApiResponseEnum {
    // 200
    NOTE_CREATE_SUCCESS(HttpStatus.OK,"노트(회의록) 생성에 성공하였습니다."),
    NOTE_LIST_GET_SUCCESS(HttpStatus.OK,"노트(회의록) 전체 조회에 성공하였습니다."),
    NOTE_GET_SUCCESS(HttpStatus.OK,"노트(회의록) 단건 조회에 성공하였습니다."),
    NOTE_SEARCH_SUCCESS(HttpStatus.OK,"노트(회의록) 검색에 성공하였습니다."),
    NOTE_UPDATE_SUCCESS(HttpStatus.OK,"노트(회의록) 수정에 성공하였습니다."),
    NOTE_DELETE_SUCCESS(HttpStatus.OK,"노트(회의록) 삭제에 성공하였습니다."),

    NOT_THIS_PROJECT_NOTICE(HttpStatus.FORBIDDEN,"해당 프로젝트의 노트가 아닙니다."),
    NOTE_NOT_EXIST(HttpStatus.FORBIDDEN,"존재하지 않는 회의록 입니다."),
    NOT_ENOUGH_PERMISSION(HttpStatus.FORBIDDEN,"해당 기능을 사용할 수 있는 권한이 아닙니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    ApiResponseNoteEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
        code = httpStatus.value();
    }
}
