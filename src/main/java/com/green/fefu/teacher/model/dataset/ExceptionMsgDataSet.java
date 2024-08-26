package com.green.fefu.teacher.model.dataset;

public interface ExceptionMsgDataSet {
    String BIRTH_TYPE_ERROR = "생년월일 입력 형식 에러";
    String ID_PATTERN_ERROR = "아이디 형식을 맞춰주세요";
    String EMAIL_PATTERN_ERROR = "이메일 형식을 맞춰주세요";
    String NAME_PATTERN_ERROR = "이름 형식을 맞춰주세요";
    String PASSWORD_PATTERN_ERROR = "비밀번호 형식을 맞춰주세요";
    String PHONE_PATTERN_ERROR = "휴대폰 번호 형식을 맞춰주세요";
    String QUERY_RESULT_ERROR = "쿼리 에러";
    String ID_NOT_FOUND_ERROR = "아이디가 일치하지 않습니다.";
    String PASSWORD_NO_MATCH_ERROR = "비밀번호가 일치하지 않습니다.";
    String ESSENTIAL_DATA_NOT_FOUND_ERROR = "필수 데이터가 없습니다.";
    String DUPLICATE_DATA_ERROR = "이미 사용 중 입니다.";
    String NOT_FOUND_USER_ERROR = "유저 정보가 없습니다.";
    String SMS_SEND_ERROR = "문자 메세지 보내기 실패";
    String MULTIPLE_DATA_ERROR = "필요한 값을 초과했습니다.";
    String STUDENT_GRADE_DATA_ERROR = "학생의 학년/반 입력을 확인 해주세요.";
    String ADDR_DATA_ERROR = "주소값 혹은 우편번호 값이 부족합니다.";
    String COOKIE_NOT_FOUND_ERROR = "쿠키에 저장된 데이터가 없습니다.";
    String RE_FRESH_TOKEN_TIME_OUT_ERROR = "리프레쉬 토큰의 만료시간이 초과하였습니다.";
    String STUDENT_GRADE_NUMBER_DATA_ERROR = "학생의 학년/반/번호 입력을 확인 해주세요.";
}
