package com.dolloer.colla.security;


import lombok.Getter;

@Getter
public class AuthUser {
    private final Long userId;
    private final String name;
    private final String email;

    public AuthUser(Long userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }
}