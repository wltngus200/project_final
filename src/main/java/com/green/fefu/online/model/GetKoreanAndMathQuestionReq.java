package com.green.fefu.online.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetKoreanAndMathQuestionReq {
    //과목, 학년, 난이도
    @Schema(example = "1", description = "학부모의 경우 한 명의 자녀 PK가 필요함", nullable = true)
    private Long studentPk; // 학년 정보를 가져옴

    @Schema(example = "1", description = "1 -> 국어 2 -> 수학")
    private Long subjectCode; // 과목


}
