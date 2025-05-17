package com.dolloer.colla.response.exception;


import com.dolloer.colla.response.response.ApiResponseEnum;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ApiResponseEnum apiResponseEnum;

    public CustomException(ApiResponseEnum apiResponseEnum) {
        super(apiResponseEnum.getMessage());
        this.apiResponseEnum = apiResponseEnum;
    }
}