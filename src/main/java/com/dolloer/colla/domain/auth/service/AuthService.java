package com.dolloer.colla.domain.auth.service;

import com.dolloer.colla.domain.auth.dto.response.LoginResponse;
import com.dolloer.colla.domain.auth.dto.response.SignupResponse;
import com.dolloer.colla.domain.auth.entity.Member;
import com.dolloer.colla.domain.auth.repository.AuthRepository;
import com.dolloer.colla.response.exception.CustomException;
import com.dolloer.colla.response.response.ApiResponseAuthEnum;
import com.dolloer.colla.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public SignupResponse signUp(String username, String email, String password) {

        authRepository.findByEmail(email)
                .ifPresent(member -> {
                    throw new CustomException(ApiResponseAuthEnum.MEMBER_ALREADY_EXIST);
                });

        Member member = new Member(username, email, passwordEncoder.encode(password));
        authRepository.save(member);
        return new SignupResponse(member.getId(), member.getUsername(), member.getEmail());
    }

    public LoginResponse logIn(String email, String password){

        Member member = authRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ApiResponseAuthEnum.MEMBER_NOT_EXIST));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new CustomException(ApiResponseAuthEnum.MEMBER_WRONG_PASSWORD);
        }

        return new LoginResponse(jwtUtil.createToken(member.getId(), member.getUsername(), member.getEmail()));
    }
}
