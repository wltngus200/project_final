package com.green.fefu.common;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ResultDto<T> {
    private HttpStatus statusCode;
    private String resultMsg;
    private T resultData;


    private Integer code;
    private String msg;
    private T data;




    public static <T> ResultDto<T> resultDto(Integer code, String msg, T data) {
        return ResultDto.<T>builder()
                .code(code)
                .msg(msg)
                .data(data)
                .build();
    }
    public static <T> ResultDto<T> resultDto1(Integer code, String msg) {
        return ResultDto.<T>builder()
                .code(code)
                .msg(msg)
                .build();
    }
}



