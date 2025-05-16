package com.dolloer.colla.response.exception;


import com.dolloer.colla.response.response.ApiResponseAuthEnum;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ApiResponseAuthEnum apiResponseAuthEnum;

    public CustomException(ApiResponseAuthEnum apiResponseAuthEnum) {
        super(apiResponseAuthEnum.getMessage());
        this.apiResponseAuthEnum = apiResponseAuthEnum;
    }
}