package com.dolloer.colla.response.exception;



import com.dolloer.colla.response.response.ApiResponse;
import com.dolloer.colla.response.response.ApiResponseAuthEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // CustomException 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<?>> handleCustomException(CustomException ex) {

        ApiResponseAuthEnum errorEnum = ex.getApiResponseAuthEnum();

        log.warn("CustomException 발생: [{}] {}", errorEnum.getHttpStatus(), errorEnum.getMessage());

        return ResponseEntity
            .status(ex.getApiResponseAuthEnum().getHttpStatus()) // HTTP 상태 코드 설정
            .body(ApiResponse.error(ex.getApiResponseAuthEnum().getMessage())); // 에러 메시지 응답
    }

    // 기타 예외 처리 (NULL 포인터, ILLEGAL ARGUMENT 등)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception ex) {
        log.error("서버 내부 예외 발생", ex);
        return ResponseEntity
            .status(500)
            .body(ApiResponse.error("서버 내부 오류가 발생했습니다."));
    }
}