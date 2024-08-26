package com.green.fefu.exception.ljm;

import com.green.fefu.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum LjmErrorCode implements ErrorCode {
    ESSENTIAL_INPUT_MATTERS(HttpStatus.BAD_REQUEST, "아이디와 비밀번호는 필수 입력사항입니다.") ,
    ID_PATTERN_ERROR(HttpStatus.BAD_REQUEST, "아이디 형식이 잘못되었습니다.") ,
    PASSWORD_PATTERN_ERROR(HttpStatus.BAD_REQUEST, "비밀번호 형식이 잘못되었습니다.") ,
    EMAIL_PATTERN_ERROR(HttpStatus.BAD_REQUEST, "이메일 형식이 잘못되었습니다.") ,
    SIGN_UP_FAIL(HttpStatus.BAD_REQUEST, "회원가입에 실패했습니다.") ,
    STUDENT_INFORMATION_INPUT(HttpStatus.BAD_REQUEST,"학생 정보를 등록 해 주세요") ,
    NOT_EXISTENCE_REQUEST(HttpStatus.BAD_REQUEST, "해당 요청에 대한 정보가 존재하지 않습니다.") ,
    ID_CHECK_PLEASE(HttpStatus.BAD_REQUEST, "아이디를 확인해 주세요") ,
    CHECK_ID_AND_PASSWORD(HttpStatus.BAD_REQUEST, "아이디 및 비밀번호가 잘못되었습니다.") ,
    YET_OK_USER(HttpStatus.BAD_REQUEST, "아직 승인되지않은 아이디 입니다.") ,
    NULL_HTTP_SERVLET_REQUEST(HttpStatus.BAD_REQUEST, "HttpServletRequest 값이 없습니다.") ,
    NOT_EQUAL_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "Refresh token cookie 값이 맞지않습니다.") ,
    NOT_INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "Refresh token 이 유효하지 않습니다.") ,
    FAIL_USER_FROM_TOKEN(HttpStatus.BAD_REQUEST, "사용자 정보가 맞지않습니다.") ,
    NOT_EXISTENCE_USER(HttpStatus.BAD_REQUEST, "연동된 사용자 정보가 없습니다.") ,
    NOT_EXISTENCE_SIGNATURE_FILE(HttpStatus.BAD_REQUEST, "서명 파일이 없습니다.") ,
    ERROR_SIGNATURE(HttpStatus.BAD_REQUEST, "서명 등록 오류가 발생했습니다.") ,
    NOT_ACCESS_AUTHORITY(HttpStatus.BAD_REQUEST, "접근 권한이 없습니다."),
    NOT_FOUND_PERISTALSIS_ID(HttpStatus.BAD_REQUEST, "연동된 아이디가 없습니다."),
    ERROR_SIGNATURE_CODE(HttpStatus.BAD_REQUEST, "서명오류가 발생했습니다.") ,
    EXISTENCE_PARENT(HttpStatus.BAD_REQUEST, "가입된 아이디가 있습니다.") ,
    NOT_EXISTENCE_STUDENT(HttpStatus.BAD_REQUEST, "학생 정보가 없습니다.") ,
    NOT_EXISTENCE_PARENT(HttpStatus.BAD_REQUEST, "유저정보가 없습니다.") ,
    OLD_PASSWORD_NOT_FOUND(HttpStatus.BAD_REQUEST, "기존 비밀번호가 잘못되었습니다.")
    ;

    private final HttpStatus httpStatus ;
    private final String message ;
}
