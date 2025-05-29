package com.dolloer.colla.oauth.repository;

import com.dolloer.colla.oauth.entity.OAuthToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OAuthTokenRepository extends JpaRepository<OAuthToken, Long> {
    Optional<OAuthToken> findByProviderAndPrincipalName(String provider, String principalName);
}