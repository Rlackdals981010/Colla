package com.dolloer.colla.domain.auth.service;

import com.dolloer.colla.domain.auth.entity.Member;
import com.dolloer.colla.domain.auth.repository.AuthRepository;
import com.dolloer.colla.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthRepository authRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Member member = authRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new UsernameNotFoundException("회원이 존재하지 않습니다."));
        return new AuthUser(member);
    }
}