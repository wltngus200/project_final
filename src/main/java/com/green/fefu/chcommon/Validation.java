package com.green.fefu.chcommon;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
public class Validation {

    //             벨리데이션 체크
//        1. 데이터 널체크
    public void nullCheck(final String str) throws Exception {
        if (str == null || str.trim().isBlank()) {
            throw new RuntimeException("필수값이 들어오지 않았습니다.");
        }
    }

    //        2. 데이터 타입 체크
    public <T> void typeCheck(final String str, Class<T> clazz, String msg) throws Exception {
//    int값 변환
        if (clazz == Integer.class) {
            isInteger(str, msg);
        }
//    long값 변환
        else if (clazz == Long.class) {
            isLong(str, msg);
        }
//    dataTime값 변환
        else if (clazz == LocalDate.class) {
            isDateTime(str, msg);
        } else {
            throw new RuntimeException("데이터 타입 형식 추가 바람");
        }
    }

    private void isInteger(final String str, String msg) throws Exception {
        try {
            Integer.parseInt(str);
        } catch (Exception e) {
            throw new RuntimeException(msg);
        }
    }

    private void isLong(final String str, String msg) throws Exception {
        try {
            Long.parseLong(str);
        } catch (Exception e) {
            throw new RuntimeException(msg);
        }
    }

    private void isDateTime(final String str, String msg) throws Exception {
        try {
            LocalDate.parse(str);
        } catch (Exception e) {
            throw new RuntimeException(msg);
        }
    }

    //        3. DB 데이터 길이 체크
    public void lengthCheck(final String str, int maxLength) throws Exception {
        int length = 0;
        log.info("str : {}", str);
        for (char c : str.toCharArray()) {
            // 한글 문자 유니코드 범위: \uAC00 (가) ~ \uD7A3 (힣)
            if (c >= '\uAC00' && c <= '\uD7A3') {
                length += 2; // 한글 문자는 2글자
            } else {
                length += 1; // 영어, 숫자 등은 1글자
            }
        }

        if (length > maxLength) {
            throw new RuntimeException("글의 길이가 너무 깁니다.");
        }
    }
}
