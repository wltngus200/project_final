package com.green.fefu.exception;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.green.fefu.exception.LJH.LjhErrorCode.SCORE_OVER_POST;

@Slf4j
@RestControllerAdvice //Advice라는 단어가 보이면 AOP
//AOP(Aspect Oriented Programing =관점지향 프로그래밍)
//Exception을 잡아낸다(모두는 아님, 하지만 가능은 함-내가 작성해 줄 경우-)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // 커스텀한 예외가 발생되었을 경우 캐치
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException(CustomException e) {
        log.error("CustomException - handlerException : {}", e.getMessage());
        return handleExceptionInternal(e.getErrorCode());
    }

//    벨리데이션 에러 터졌을때
    @Override
//    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("MethodArgumentNotValidException - handlerException : {}", ex.getMessage());
        return handleExceptionInternal(
                CustomErrorCode.VALIDATION_ERROR,
                ex
        );
    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
        if (errorCode == null) {
            log.error("ErrorCode is null - handlerException");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unknown error occurred");
        }

        log.error("ErrorCode - handlerException : {}", errorCode);
        return handleExceptionInternal(errorCode, null);
    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode, BindException e) {
        log.error("ErrorCode - handlerException : {}", errorCode);
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode, e));
    }

    private MyErrorResponse makeErrorResponse(ErrorCode errorCode, BindException e) {
        log.error("ErrorCode - handlerException : {}", errorCode);
        return MyErrorResponse.builder()
                .statusCode(errorCode.getHttpStatus())
                .resultMsg(errorCode.getMessage())
                .result(errorCode.name())
                .valids(e == null
                        ? null
                        // validation 에러 메시지를 정리
                        : getValidationError(e)
                )
                .build();
    }

    private List<MyErrorResponse.ValidationError> getValidationError(BindException e) {
        log.error("ErrorCode - handlerException : {}", e);
        List<MyErrorResponse.ValidationError> list = new ArrayList<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            list.add(MyErrorResponse.ValidationError.of(fieldError));
        }
        return list;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleExceptions(Exception e) {
        log.error("Exception - handlerException : {}", e.getMessage());
        e.printStackTrace();
        return handleExceptionInternal(
               CustomErrorCode.VALIDATION_ERROR
        );
    }
}
