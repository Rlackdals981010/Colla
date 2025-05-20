package com.dolloer.colla.domain.auth.entity;

import com.dolloer.colla.security.AuthUser;
import jakarta.persistence.*;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    public Member(String username, String email, String password) {
        this.username=username;
        this.email=email;
        this.password = password;
    }




}

