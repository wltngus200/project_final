package com.green.fefu.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class CookieUtils {

    private final ObjectMapper om;

    public <T> T getData(T type, HttpServletRequest req, String name) {
        try {
            Cookie cookie = getCookie(req, name);
            String json = cookie.getValue();
            return (T) om.readValue(json, type.getClass());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //요청 header에 내가 원하는 쿠키를 찾는 메소드
    public Cookie getCookie(HttpServletRequest req, String name) {
        // req에서 모든 쿠키 정보를 받는다.
        Cookie[] cookies = req.getCookies();

        //쿠키 정보가 있고 쿠키가 하나이상 있다면
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {

                //찾고자 하는 key가 있는지 확인 후 있으면 리턴
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    public <T> T getCookie (HttpServletRequest req, String name, Class<T> valueType) {
        Cookie cookie = getCookie(req, name);
        if(cookie == null) { return null;}
        if(valueType == String.class) {
            return (T) cookie.getValue();
        }
        return deserialize(cookie,valueType);
    }

    public void setCookie(HttpServletResponse res, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        // Root URL ( 우리 백엔드 모든 요청에 해당하게 셋팅 )
        cookie.setPath("/");
        // 보안 쿠키
        cookie.setHttpOnly(true);
        // 만료 시간
        cookie.setMaxAge(maxAge);
        res.addCookie(cookie);
    }

    //value에 객체를 넣으면 json형태로 변환해서 cookie에 저장
    public void setCookie(HttpServletResponse res, String name, Object obj, int maxAge) {
        this.setCookie(res,name,serialize(obj),maxAge);
    }

    public void deleteCookie(HttpServletResponse res, String name) {
        setCookie(res, name, null, 0);
    }

    public String serialize(Object obj) {
        return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(obj));
    }
    public <T> T deserialize(Cookie cookie,Class<T> cls){
        return cls.cast(
            SerializationUtils.deserialize(
                    Base64.getUrlDecoder().decode(cookie.getValue())
                    // 객체가 가지고있는 데이터를 문자열로 변환
                    //String >  byte[] > Object
            )
        );
    }
}
