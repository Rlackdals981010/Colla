package com.dolloer.colla.domain.health;

import com.dolloer.colla.response.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Void>> healthCheck(){
        return ResponseEntity.ok(ApiResponse.success("동작중"));
    }
}
