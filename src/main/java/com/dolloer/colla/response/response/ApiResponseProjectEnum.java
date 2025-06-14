package com.dolloer.colla.response.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiResponseProjectEnum implements ApiResponseEnum {
    // 200
    PROJECT_CREATE_SUCCESS(HttpStatus.OK,"프로젝트 생성에 성공하였습니다."),
    PROJECT_LIST_GET_SUCCESS(HttpStatus.OK,"프로젝트 목록 조회에 성공하였습니다."),
    PROJECT_GET_SUCCESS(HttpStatus.OK,"프로젝트 단건 조회에 성공하였습니다."),
    PROJECT_INVITE_SUCCESS(HttpStatus.OK,"프로젝트 초대에 성공하였습니다."),
    PROJECT_LEAVE_SUCCESS(HttpStatus.OK,"프로젝트 탈퇴에 성공하였습니다."),
    PROJECT_MEMBERS_GET_SUCCESS(HttpStatus.OK,"프로젝트 멤버 조회에 성공하였습니다."),
    PROJECT_MEMBERS_DELETE_SUCCESS(HttpStatus.OK,"프로젝트 멤버 강퇴에 성공하였습니다."),
    PROJECT_MEMBER_ROLE_CHANGE_SUCCESS(HttpStatus.OK,"권한 변경에 성공하였습니다."),


    MEMBER_NOT_EXIST(HttpStatus.BAD_REQUEST,"본 서비스 회원이 아닙니다."),


    // 403
    NOT_PROJECT_MEMBER(HttpStatus.FORBIDDEN,"존재하지 않는 맴버 입니다."),
    NOT_THIS_PROJECT_MEMBER(HttpStatus.FORBIDDEN,"해당 프로젝트의 맴버가 아닙니다."),
    NOT_ENOUGH_PERMISSION(HttpStatus.FORBIDDEN,"해당 기능을 사용할 수 있는 권한이 아닙니다."),
    CANNOT_REMOVE_SELF(HttpStatus.FORBIDDEN,"스스로는 강퇴가 불가능 합니다."),
    CANNOT_REMOVE_OWNER(HttpStatus.FORBIDDEN,"OWNER는 강퇴가 불가능 합니다."),
    ALREADY_RESPONDED(HttpStatus.FORBIDDEN,"이미 응답한 상태입니다."),
    OWNER_CANNOT_LEAVE(HttpStatus.FORBIDDEN,"프로젝트 생성자는 탈퇴할 수 없습니다."),
    CANNOT_CHANGE_OWN_ROLE(HttpStatus.FORBIDDEN,"본인의 권한은 변경할 수 없습니다."),
    ROLE_ALREADY_ASSIGNED(HttpStatus.FORBIDDEN,"동일한 권한입니다."),
    CANNOT_DOWNGRADE_OWNER(HttpStatus.FORBIDDEN,"소유자의 권한을 내릴 수 없습니다."),

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
