package com.green.fefu.exception.JSH;

import com.green.fefu.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
@Getter
@RequiredArgsConstructor
public enum JshErrorCode implements ErrorCode {
    OMR_ISN_T_CORRECT(HttpStatus.BAD_REQUEST,"문항의 수와 정답의 수가 일치하지 않습니다."),
    NOTICE_STATE_CHECK(HttpStatus.BAD_REQUEST,"정상적이지 않은 알림장 항목을 호출하였습니다."),
    EXCEED_PK_VALUE(HttpStatus.BAD_REQUEST, "해당문제를 데이터베이스에서 조회할 수 없습니다."),
    HOMEROOM_ISN_T_EXIST(HttpStatus.BAD_REQUEST, "맡은 학급을 조회할 수 없습니다."),
    HAS_NOT_GRADE(HttpStatus.BAD_REQUEST, "학년 정보를 조회할 수 없습니다."),
    HAS_NOT_CLASS(HttpStatus.BAD_REQUEST, "각 학년의 담임 선생님만 출제할 수 있습니다."),
    CAN_T_UPLOAD_QUESTION(HttpStatus.BAD_REQUEST, "문제를 등록하지 못 했습니다"),
    NOT_FOUND_QUESTION(HttpStatus.BAD_REQUEST, "문제를 불러올 수 없습니다."),
    STUDENT_PK_NOT_FOUND_ERROR(HttpStatus.BAD_REQUEST, "학생 데이터 에러"),
    CAN_T_GET_GRADE(HttpStatus.BAD_REQUEST, "학년 정보를 읽어올 수 없습니다.");
    private final HttpStatus httpStatus;
    private final String message;
}
