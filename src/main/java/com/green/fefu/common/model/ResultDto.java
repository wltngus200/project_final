package com.green.fefu.common.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@SuperBuilder
@Getter
@EqualsAndHashCode
public class ResultDto<T>{
    private HttpStatusCode statusCode;
    private String resultMsg;
    private T result;
}
