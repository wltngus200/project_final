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
public class StudentOmr { //프론트가 나에게 넘겨줄 값
    @Schema(name="questionPk", example = "[1,5,2,6,8 ...]")
    private List<Long> questionPk; //현재 화면에 출력되고 있는 문제의 PK값 리스트
    @Schema(name="omrAnswer", example = "[3,4,2,1,2,4 ...]")
    private List<Integer> omrAnswer; //학생이 제출 OMR

    @Schema(example="240813 국어모의고사", description = "오답노트에 사용될 제목입니다.")
    private String title;

    @Schema(example = "1", description = "국어-> 1 수학-> 2")
    private Long subjectCode;
}
