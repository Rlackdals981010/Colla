package com.dolloer.colla.domain.auth.entity;

import jakarta.persistence.*;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
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

    public Long getId(){
        return this.id;
    }
    public String getUsername(){
        return this.username;
    }
    public String getEmail(){
        return this.email;
    }

}

