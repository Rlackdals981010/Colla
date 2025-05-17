package com.dolloer.colla.response.response;

import org.springframework.http.HttpStatus;

public interface ApiResponseEnum {
    HttpStatus getHttpStatus();
    String getMessage();
}
