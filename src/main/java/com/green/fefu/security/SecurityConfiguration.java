package com.green.fefu.security;

import com.green.fefu.common.AppProperties;
import com.green.fefu.security.jwt.JwtAuthenticationAccessDeniedHandler;
import com.green.fefu.security.jwt.JwtAuthenticationEntryPoint;
import com.green.fefu.security.jwt.JwtAuthenticationFilter;
import com.green.fefu.security.oauth2.MyOAuth2UserService;
import com.green.fefu.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.green.fefu.security.oauth2.OAuth2AuthenticationRequestBasedOnCookieRepository;
import com.green.fefu.security.oauth2.OAuth2AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfiguration {
    //@Component로 빈등록을 하였기 때문에 DI가 된다.
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final OAuth2AuthenticationRequestBasedOnCookieRepository repository;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final MyOAuth2UserService myOAuth2UserService;
    private final AppProperties appProperties;

    /*
      메소드 빈등록으로 주로쓰는 케이스는 (현재 기준으로 설명하면) Security와 관련된
      빈등록을 여러개 하고 싶을 때 메소드 형식으로 빈등록 하면 한 곳에 모을 수가 있으니 좋다.
        메소드 빈등록으로 하지 않으면 각각 클래스로 만들어야 한다.
     */

    @Bean //메소드 타입의 빈 등록 (파라미터, 리턴타입 중요) 파라미터는 빈등록할때 필요한 객체
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        //파라미터없이 내가 직접 new 객체화해서 리턴으로 빈등록 가능

        CommonOAuth2Provider a;

        return httpSecurity.sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ) //시큐리티에서 세션 사용을 하지 않음을 세팅
                .httpBasic(http -> http.disable())
                // (SSR 서버사이드 렌더링 하지 않는다. 즉 html화면을 백엔드가 만들지 않는다.)
                // 백엔드에서 화면을 만들지 않더라도 위 세팅을 끄지 않아도 괜찮다. 사용하지 않는 걸 끔으로써 리소스 확보
                // 하기 위해서 사용하는 개념
                // 정리하면, 시큐리티에서 제공해주는 로그인 화면 사용하지 않겠다.
                .formLogin(form -> form.disable()) //form 로그인 방식을 사용하지 않음을 세팅
                .csrf(csrf -> csrf.disable()) //CSRF (CORS랑 많이 헷갈려 함)
                //requestMatchers
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/feed").authenticated()
                                .requestMatchers(
                                        "/api/feed"
                                        , "/api/feed/*"
                                        , "/api/user/pic"
                                )
                                .authenticated()
                                .anyRequest()
                                .permitAll()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                        .accessDeniedHandler(new JwtAuthenticationAccessDeniedHandler())
                )
                .oauth2Login( oauth2 -> oauth2.authorizationEndpoint(
                                        auth -> auth.baseUri("/oauth2/authorization")
                                                .authorizationRequestRepository(repository)
                                )
                                .redirectionEndpoint( redirection -> redirection.baseUri("/*/oauth2/code/*"))
                                .userInfoEndpoint(userInfo -> userInfo.userService(myOAuth2UserService))
                                .successHandler(oAuth2AuthenticationSuccessHandler)
                                .failureHandler(oAuth2AuthenticationFailureHandler)
                )
                /*
                //만약, permitAll메소드가 void였다면 아래와 같이 작성을 해야함
                .authorizeHttpRequests(auth -> {
                    //
                    auth.requestMatchers("/api/user/sign-up","/api/user/sign-in").permitAll();
                    auth.requestMatchers("/api/ddd").authenticated();
                })
                //permitAll 메소드가 자기 자신의 주소값을 리턴한다면 체이닝 기법 가능
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/api/user/sign-up","/api/user/sign-in").permitAll()
                        .requestMatchers("/api/ddd").authenticated();
                })
                */
                .build();
/*
        //위 람다식을 풀어쓰면 아래와 같다. 람다식은 짧게 적을 수 있는 기법.
        return httpSecurity.sessionManagement(new Customizer<SessionManagementConfigurer<HttpSecurity>>() {
            @Override
            public void customize(SessionManagementConfigurer<HttpSecurity> session) {
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            }
        }).build();
*/
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
