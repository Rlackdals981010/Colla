package com.dolloer.colla.response.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiResponseFileEnum implements ApiResponseEnum {
    // 200
    FIlE_UPLOAD_SUCCESS(HttpStatus.OK,"파일 업로드에 성공하였습니다."),
    FIlE_LIST_GET_SUCCESS(HttpStatus.OK,"파일 목록 조회에 성공하였습니다."),
    FIlE_DELETE_SUCCESS(HttpStatus.OK,"파일 삭제에 성공하였습니다."),
    FIlE_UPDATE_SUCCESS(HttpStatus.OK,"파일 수정에 성공하였습니다."),
    FIlE_DETAIL_GET_SUCCESS(HttpStatus.OK,"파일 단건 조회에 성공하였습니다."),
    FIlE_LIST_SEARCH_SUCCESS(HttpStatus.OK,"파일 목록 검색에 성공하였습니다."),

    // 400
    DUPLICATED_LINK(HttpStatus.BAD_REQUEST,"동일한 링크가 존재합니다."),

    //401
    MEMBER_WRONG_PASSWORD(HttpStatus.UNAUTHORIZED,"비밀번호가 틀렸습니다."),

    // 403
    NO_UPDATE_REQUESTED(HttpStatus.FORBIDDEN,"수정 내용이 없습니다."),
    FILE_NOT_FOUND(HttpStatus.FORBIDDEN,"존재하지 않는 파일 입니다."),
    FILE_PROJECT_MISMATCH(HttpStatus.FORBIDDEN,"해당 프로젝트의 파일이 아닙니다."),
    NOT_PROJECT_MEMBER(HttpStatus.FORBIDDEN,"존재하지 않는 맴버 입니다."),
    NOT_ENOUGH_PERMISSION(HttpStatus.FORBIDDEN,"해당 기능을 사용할 수 있는 권한이 아닙니다."),

    //404
    LINK_NOT_EXIST(HttpStatus.NOT_FOUND,"존재하지 않는 링크 글 입니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    ApiResponseFileEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
        code = httpStatus.value();
    }
}
