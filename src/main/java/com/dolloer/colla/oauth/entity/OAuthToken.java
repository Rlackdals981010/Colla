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
@NoArgsConstructor
@AllArgsConstructor
public class OAuthToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String provider;          // ex. "google"
    @Setter
    private String principalName;     // ex. Google 계정 식별자
    @Setter
    private String accessToken;

    @Setter
    @Column(nullable = false)
    private String refreshToken;

    @Setter
    private LocalDateTime expiresAt;

    private LocalDateTime createdAt = LocalDateTime.now();
}