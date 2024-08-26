package com.green.fefu.chcommon;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public interface ResponsDataSet {
    // 정상 결과
    HttpStatus OK = HttpStatus.OK;
    // 결과 에러
    HttpStatus NOT_FOUND = HttpStatus.NOT_FOUND;


}
