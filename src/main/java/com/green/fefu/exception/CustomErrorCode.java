package com.green.fefu.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
@Getter
@RequiredArgsConstructor
public enum CustomErrorCode implements ErrorCode{

    CLASS_CHECK_PLEASE(HttpStatus.NOT_FOUND, "호출한 학급이 존재하지 않습니다."),

    YOU_ARE_NOT_TEACHER(HttpStatus.FORBIDDEN, "선생님만 접근할 수 있습니다."),

    VALIDATION_ERROR(HttpStatus.BAD_REQUEST,"데이터 형식을 맞춰주세요");

    //권한 없음, 토큰 만료, 뭐가 또 있지........?!
    //2학기 아직 시작 안 했는데 2학기 조회라던가 "해당학기의 정보를 조회할 수 없습니다."
    //아이디 비번 일치, 회원정보, 비밀번호나 다른거 정규식,
    //점수 값 이상함 101점이라거나, -1점
    //블랙리스트 올라감 ㅎ

    private final HttpStatus httpStatus;
    private final String message;
}
