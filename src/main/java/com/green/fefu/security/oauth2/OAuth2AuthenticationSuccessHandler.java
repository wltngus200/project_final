package com.green.fefu.security.oauth2;

import com.green.fefu.common.AppProperties;
import com.green.fefu.common.CookieUtils;
import com.green.fefu.security.MyUser;
import com.green.fefu.security.MyUserDetails;
import com.green.fefu.security.MyUserOAuth2Vo;
import com.green.fefu.security.jwt.JwtTokenProviderV2;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

/*
    인증받고 싶은 소셜 로그인 선택(redirect_uri 는 소셜로그인 마무리 되고 프론트엔드로 가는 url)
    소셜로그인 화면출력

    아이디/비번 입력해서 로그인 시도 (redirect_uri 는 백엔드로 가는 url)
    인가 코드를 받기위한 작업이 이루저임
    제공자 provider는 아이디/비번이 일치한다면 백엔드 redirect_uri로 인가코드를보내줌
    백엔드 인가코드로 access_token 을 받기 위한 작업이 이루어짐
    백엔드 access _token 으로 사용자 정보를 받기 위한 작업이 이루어짐
    로컬 로그인 작업 수행 SuccessHandler
    프론트 엔드 redirect_uri 로 리다이렉트를 하면서 필요한 정보를 파라미터로 보내준다

 */

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler
        extends SimpleUrlAuthenticationSuccessHandler {

    private final OAuth2AuthenticationRequestBasedOnCookieRepository repository;
    private final JwtTokenProviderV2 jwtTokenProvider;
    private final AppProperties appProperties;
    private final CookieUtils cookieUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);
        log.info("targetUrl: {}", targetUrl);
        if(response.isCommitted()) { //응답 객체가 만료된 경우(다른곳에서 응답처리를 한 경우)
            log.error("onAuthenticationSuccess - 응답이 만료됨");
            return;
        }
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String redirectUri = cookieUtils.getCookie(request
                                                 , appProperties.getOauth2().getRedirectUriParamCookieName()
                                                 , String.class);

        // (yaml) app.oauth2.uthorized-redirect-uris 리스트에 없는 Uri인 경우
        if(redirectUri != null && !hasAuthorizedRedirectUri(redirectUri)) {
            throw new IllegalArgumentException("인증되지 않은 Redirect URI입니다.");
        }

        //FE가 원하는 redirect_url값이 저장
        String targetUrl = redirectUri == null ? getDefaultTargetUrl() : redirectUri;

        //user_id, nm, pic, access_token를 FE에게 리턴
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        MyUserOAuth2Vo myUserOAuth2Vo = (MyUserOAuth2Vo)myUserDetails.getMyUser();
        MyUser myUser = MyUser.builder()
                .userId(myUserOAuth2Vo.getUserId())
                .role(myUserOAuth2Vo.getRole())
                .build();

        String accessToken = jwtTokenProvider.generateAccessToken(myUser);
        String refreshToken = jwtTokenProvider.generateRefreshToken(myUser);

        //refreshToken은 보안 쿠키를 이용해서 처리(FE가 따로 작업을 하지 않아도 아래 cookie값은 항상 넘어온다.)
        int refreshTokenMaxAge = appProperties.getJwt().getRefreshTokenCookieMaxAge();
        cookieUtils.deleteCookie(response, appProperties.getJwt().getRefreshTokenCookieName());
        cookieUtils.setCookie(response
                            , appProperties.getJwt().getRefreshTokenCookieName()
                            , refreshToken
                            , refreshTokenMaxAge);

        //http://localhost:8080/oauth/redirect?user_id=1&nm=홍길동&pic=https://image.jpg&access_token=aslkdjslajf
        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("user_id", myUserOAuth2Vo.getUserId())
                .queryParam("nm", myUserOAuth2Vo.getNm())
                .queryParam("pic", myUserOAuth2Vo.getPic())
                .queryParam("access_token", accessToken)
                .build()
                .toUriString();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        repository.removeAuthorizationRequestCookies(response);
    }

    //우리가 설정한 redirect-uri인지 체크
    private boolean hasAuthorizedRedirectUri(String uri) {
        URI savedCookieRedirectUri = URI.create(uri);
        log.info("savedCookieRedirectUri.getHost(): {}", savedCookieRedirectUri.getHost());
        log.info("savedCookieRedirectUri.getPort(): {}", savedCookieRedirectUri.getPort());

        for(String redirectUri : appProperties.getOauth2().getAuthorizedRedirectUris()) {
            URI authorizedUri = URI.create(redirectUri);
            if(savedCookieRedirectUri.getHost().equalsIgnoreCase(authorizedUri.getHost())
                    && savedCookieRedirectUri.getPort() == authorizedUri.getPort()) {
                return true;
            }
        }
        return false;
    }
}
