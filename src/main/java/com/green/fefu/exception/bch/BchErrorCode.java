package com.green.fefu.exception.bch;

import com.green.fefu.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import static com.green.fefu.teacher.model.dataset.ExceptionMsgDataSet.*;

@Getter
@RequiredArgsConstructor
public enum BchErrorCode implements ErrorCode {

    ADDR_DATA_ERROR(HttpStatus.BAD_REQUEST, "주소값 혹은 우편번호 값이 부족합니다."),
    BIRTH_TYPE_ERROR(HttpStatus.BAD_REQUEST, "생년월일 입력 형식 에러"),
    ID_PATTERN_ERROR(HttpStatus.BAD_REQUEST, "아이디 형식을 맞춰주세요"),
    EMAIL_PATTERN_ERROR(HttpStatus.BAD_REQUEST, "이메일 형식을 맞춰주세요"),
    NAME_PATTERN_ERROR(HttpStatus.BAD_REQUEST, "이름 형식을 맞춰주세요"),
    PASSWORD_PATTERN_ERROR(HttpStatus.BAD_REQUEST,"비밀번호 형식을 맞춰주세요"),
    PHONE_PATTERN_ERROR(HttpStatus.BAD_REQUEST,"휴대폰 번호 형식을 맞춰주세요"),
    QUERY_RESULT_ERROR(HttpStatus.BAD_REQUEST,"쿼리 에러"),
    ID_NOT_FOUND_ERROR(HttpStatus.BAD_REQUEST,"아이디가 일치하지 않습니다."),
    PASSWORD_NO_MATCH_ERROR(HttpStatus.BAD_REQUEST,"비밀번호가 일치하지 않습니다."),
    ESSENTIAL_DATA_NOT_FOUND_ERROR(HttpStatus.BAD_REQUEST,"필수 데이터가 없습니다."),
    DUPLICATE_DATA_ERROR(HttpStatus.BAD_REQUEST,"이미 사용 중 입니다."),
    NOT_FOUND_USER_ERROR(HttpStatus.BAD_REQUEST,"유저 정보가 없습니다."),
    SMS_SEND_ERROR(HttpStatus.BAD_REQUEST,"문자 메세지 보내기 실패"),
    MULTIPLE_DATA_ERROR(HttpStatus.BAD_REQUEST,"필요한 값을 초과했습니다."),
    STUDENT_GRADE_DATA_ERROR(HttpStatus.BAD_REQUEST,"학생의 학년/반 입력을 확인 해주세요."),
    COOKIE_NOT_FOUND_ERROR(HttpStatus.BAD_REQUEST,"쿠키에 저장된 데이터가 없습니다."),
    RE_FRESH_TOKEN_TIME_OUT_ERROR(HttpStatus.BAD_REQUEST,"리프레쉬 토큰의 만료시간이 초과하였습니다."),
    DIVISION_ERROR(HttpStatus.BAD_REQUEST, "분류 코드가 잘못되었습니다."),
    FILE_ERROR(HttpStatus.BAD_REQUEST, "파일 저장에 실패하였습니다."),
    NOT_YET_ACCEPT(HttpStatus.BAD_REQUEST, "아직 승인되지 않은 계정입니다."),
    STUDENT_ID_DUPLICATE_ERROR(HttpStatus.BAD_REQUEST, "이미 존재하는 아이디입니다."),
    GRADE_DUPLICATE_ERROR(HttpStatus.BAD_REQUEST, "학년 반 번호 값이 이미 있습니다."),
    GRADE_DATA_NOT_FOUND(HttpStatus.BAD_REQUEST, "학년 반 정보가 없습니다."),
    MULTIPLE_TEACHER_ERROR(HttpStatus.BAD_REQUEST, "해당 학급에는 담당 선생님이 있습니다."),
    MULTIPLE_PARENT_ERROR(HttpStatus.BAD_REQUEST, "이미 학부모 정보가 있습니다."),
    NOT_FOUND_CLASS_ERROR(HttpStatus.BAD_REQUEST, "학년학급 정보를 찾을 수 없습니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
