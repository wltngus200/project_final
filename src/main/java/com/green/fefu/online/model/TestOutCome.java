package com.green.fefu.online.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class TestOutCome { // 틀린 문제만 리턴-> 프론트 요구시 수정

    private StudentOmr studentOmr; //프론트가 전송한 값 그대로

    private List<Integer> realAnswer; //실제 정답 번호

    private List<String> typeString; //문제 유형

}
