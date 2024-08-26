package com.green.fefu.security.oauth2;

import com.green.fefu.common.AppProperties;
import com.green.fefu.common.CookieUtils;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
/*
 만약 토큰이 만료 되어 권한 부여 를 다시하는 경우 이전의 세션이 존재한다면 현재 사용하는 게 아니라
 예전꺼를 사용하기때문에 문제될 가능성이 있음 . 그래서 세션에서 삭제를 한다 .
 인가 인증 코드가 1회용인것처럼  OAuth2AuthorizationRequest 객체도 1회용으로 사용


 스프링 시큐리티 Oauth 처리때 사용 하는 필터가 2개가 있음.
 OAuth2AuthorizationRequestRedirectFilter AS(가 필터), OAuth2LoginAuthenticationFilter AS 나 필터

 인가 / 인증 코드는 요청 보낼떄 마다 값이 달라진다
 1단계 :OAuth2AuthorizationRequest(AS A) 는 소셜로그인 요청할 때마다 생서오디는 객체
 2단계 : Access Token 을 요청한 이후에는 A 를 사용할 일이 발생하지 않기 떄무넹 Cookie 에서 삭제

 세션을 이용해서 처리하는 방식은 확장이 불리함 . >>> 쿠키로 해결
 removeAuthorizationRequest 메소드에서 했던 것 같음.

 가 필터에서

 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationRequestBasedOnCookieRepository
        implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private final CookieUtils cookieUtils;
    private final AppProperties appProperties;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        log.info("CookieRepository - loadAuthorizationRequest");
        return cookieUtils.getCookie(request
                                    , appProperties.getOauth2().getAuthorizationRequestCookieName()
                                    , OAuth2AuthorizationRequest.class
        );
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        log.info("CookieRepository - saveAuthorizationRequest");
        if(authorizationRequest == null) { //
            this.removeAuthorizationRequestCookies(response);
            return;
        }
        cookieUtils.setCookie(response
                , appProperties.getOauth2().getAuthorizationRequestCookieName()
                , authorizationRequest
                , appProperties.getOauth2().getCookieExpirySeconds());

        //FE로 돌아갈 redirect 주소값 (즉, FE가 redirect_uri 파라미터로 백엔드에 보내준 값)
        String redirectUriAfterLogin = request.getParameter(appProperties.getOauth2().getRedirectUriParamCookieName());
        log.info("redirectUriAfterLogin: {}", redirectUriAfterLogin);
        if(StringUtils.isNotBlank(redirectUriAfterLogin)) {
            cookieUtils.setCookie(response
                    , appProperties.getOauth2().getRedirectUriParamCookieName()
                    , redirectUriAfterLogin
                    , appProperties.getOauth2().getCookieExpirySeconds()
            );
        }
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        log.info("CookieRepository - removeAuthorizationRequest");
        return this.loadAuthorizationRequest(request);
    }

    public void removeAuthorizationRequestCookies(HttpServletResponse response) {
        log.info("CookieRepository - removeAuthorizationRequestCookies");
        cookieUtils.deleteCookie(response, appProperties.getOauth2().getAuthorizationRequestCookieName());
        cookieUtils.deleteCookie(response, appProperties.getOauth2().getRedirectUriParamCookieName());
    }
}
