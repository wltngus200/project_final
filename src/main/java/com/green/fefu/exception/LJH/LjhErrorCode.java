package com.green.fefu.exception.LJH;

import com.green.fefu.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.Message;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;

@Getter
@RequiredArgsConstructor
public enum LjhErrorCode implements ErrorCode {

    SCORE_OVER_POST(HttpStatus.BAD_REQUEST,"점수가 100점 이상입니다"),

    SCORE_ROW_POST(HttpStatus.BAD_REQUEST,"점수가 0점 이하 입니다" ),

    POST_VIEW_ERROR(HttpStatus.BAD_REQUEST, "조회 권한이 없습니다."),

    SCORE_INSERT_POST(HttpStatus.BAD_REQUEST,"점수 입력 권한이 없습니다"),

    SCORE_INSERT_STU_POST(HttpStatus.BAD_REQUEST,"담당 학생이 아닙니다"),

    SCORE_GET_VIEW(HttpStatus.BAD_REQUEST,"잘못된 학년입니다."),

    SCORE_GET_LIST_VIEW(HttpStatus.BAD_REQUEST,"조회된 성적이 없습니다");

    private final HttpStatus httpStatus;
    private final String message;

}
