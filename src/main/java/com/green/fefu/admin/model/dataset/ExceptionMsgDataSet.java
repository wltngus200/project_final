package com.green.fefu.admin.model.dataset;

public interface ExceptionMsgDataSet {
    String DIVISION_CODE_ERROR = "분류 코드가 잘못되었습니다.";
    String NOT_FOUND_USER = "유저 정보가 없습니다.";
    String DIVISION_ERROR = "퇴사자 체크 데이터가 잘못되었습니다.";
    String PK_DATA_ERROR = "pk값은 음수일 수 없습니다.";
    String STATE_DATA_ERROR = "상태 값이 올바르지 않습니다.";
    String GRADE_BOUNDARY_ERROR = "학년 값이 올바르지 않습니다.";
    String CLASS_BOUNDARY_ERROR = "학급 값이 올바르지 않습니다.";
}
