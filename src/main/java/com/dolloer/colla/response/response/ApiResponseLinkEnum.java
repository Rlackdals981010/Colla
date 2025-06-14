package com.dolloer.colla.response.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiResponseLinkEnum implements ApiResponseEnum {
    // 200
    LINK_CREATE_SUCCESS(HttpStatus.OK,"링크 생성에 성공하였습니다."),
    LINK_LIST_READ_SUCCESS(HttpStatus.OK,"링크전체 조회에 성공하였습니다."),
    LINK_READ_SUCCESS(HttpStatus.OK,"단일 링크 조회에 성공하였습니다."),
    LINK_UPDATE_SUCCESS(HttpStatus.OK,"링크 수정에 성공하였습니다."),
    LINK_DELETE_SUCCESS(HttpStatus.OK,"링크 삭제에 성공하였습니다."),
    LINK_LIST_SEARCH_SUCCESS(HttpStatus.OK,"링크 검색에 성공하였습니다."),
    // 400
    DUPLICATED_LINK(HttpStatus.BAD_REQUEST,"동일한 링크가 존재합니다."),

    NOT_ENOUGH_PERMISSION(HttpStatus.FORBIDDEN,"해당 기능을 사용할 수 있는 권한이 아닙니다."),

    //404
    LINK_NOT_EXIST(HttpStatus.NOT_FOUND,"존재하지 않는 링크 글 입니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    ApiResponseLinkEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
        code = httpStatus.value();
    }
}
