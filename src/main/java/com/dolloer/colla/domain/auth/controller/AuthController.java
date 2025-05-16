package com.dolloer.colla.domain.auth.controller;


import com.dolloer.colla.domain.auth.dto.request.LoginRequest;
import com.dolloer.colla.domain.auth.dto.request.SignupRequest;
import com.dolloer.colla.domain.auth.dto.response.LoginResponse;
import com.dolloer.colla.domain.auth.dto.response.SignupResponse;
import com.dolloer.colla.domain.auth.service.AuthService;
import com.dolloer.colla.response.response.ApiResponse;
import com.dolloer.colla.response.response.ApiResponseAuthEnum;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupResponse>> signUp(@Valid @RequestBody SignupRequest signupRequest) {

        SignupResponse signupResponse = authService.signUp(signupRequest.getUsername(),signupRequest.getEmail(),signupRequest.getPassword());
        return ResponseEntity.ok(ApiResponse.success(signupResponse, ApiResponseAuthEnum.MEMBER_CREATE_SUCCESS.getMessage()));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> logIn(@Valid @RequestBody LoginRequest loginRequest) {

        LoginResponse loginResponse = authService.logIn(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(ApiResponse.success(loginResponse, ApiResponseAuthEnum.MEMBER_LOGIN_SUCCESS.getMessage()));
    }

}
