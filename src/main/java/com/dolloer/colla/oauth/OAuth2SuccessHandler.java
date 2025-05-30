package com.dolloer.colla.oauth;

import com.dolloer.colla.oauth.entity.OAuthToken;
import com.dolloer.colla.oauth.repository.OAuthTokenRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final OAuth2AuthorizedClientService authorizedClientService;
    private final OAuthTokenRepository tokenRepository;

    // OAuth2 인증 성공시 실행됨
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // 인증 객체를 OAuth2 토큰으로 다운 캐스팅해서 구글 인증 정보에 접근할 수 있께 함
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

        // 발급된 OAuth2 클라 정보 불러오기, 여기에 accesstoken 이랑 refreshtoken, 만료 시간등 ㅇㅆ음
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                oauthToken.getAuthorizedClientRegistrationId(),
                oauthToken.getName()
        );

        // client에 있는 토큰 값들 추출
        String accessToken = client.getAccessToken().getTokenValue();
        String refreshToken = client.getRefreshToken() != null
                ? client.getRefreshToken().getTokenValue()
                : null;
        LocalDateTime expiresAt = client.getAccessToken().getExpiresAt()
                .atZone(ZoneId.systemDefault()).toLocalDateTime();

        // DB에 저장할 엔티티
        OAuthToken token = tokenRepository.findByProviderAndPrincipalName(
                        oauthToken.getAuthorizedClientRegistrationId(),
                        oauthToken.getName())
                .orElse(new OAuthToken());

        token.setProvider(oauthToken.getAuthorizedClientRegistrationId());
        token.setPrincipalName(oauthToken.getName());
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setExpiresAt(expiresAt);

        tokenRepository.save(token);

        response.sendRedirect("http://localhost:3000/oauth/success"); // 원하는 클라이언트 리다이렉션 경로
    }
}