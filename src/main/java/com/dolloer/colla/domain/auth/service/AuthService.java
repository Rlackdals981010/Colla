package com.dolloer.colla.domain.auth.service;

import com.dolloer.colla.domain.auth.dto.response.SignupResponse;
import com.dolloer.colla.domain.auth.entity.Member;
import com.dolloer.colla.domain.auth.repository.AuthRepository;
import com.dolloer.colla.response.exception.CustomException;
import com.dolloer.colla.response.response.ApiResponseAuthEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    public SignupResponse signUp(String username, String email, String password) {

        if (authRepository.existsByUsername(username)) {
            throw new CustomException(ApiResponseAuthEnum.MEMBER_ALREADY_EXIST);
        }
        if (authRepository.existsByEmail(email)) {
            throw new CustomException(ApiResponseAuthEnum.MEMBER_ALREADY_EXIST);
        }


        Member member = new Member(username, email, passwordEncoder.encode(password));
        authRepository.save(member);
        return new SignupResponse(member.getId(), member.getUsername(), member.getEmail());
    }
}
