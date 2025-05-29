package com.dolloer.colla.oauth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "oauth_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OAuthToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String provider;          // ex. "google"
    private String principalName;     // ex. Google 계정 식별자
    private String accessToken;
    @Column(nullable = false)
    private String refreshToken;

    private LocalDateTime expiresAt;

    private LocalDateTime createdAt = LocalDateTime.now();
}