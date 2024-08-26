package com.green.fefu.online.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetEnglishQuestionReq {
    //학생만 풀 수 있는지 학부모도 접근 가능한지
    //학년에 따라 다르게 넣기?
    @Schema(example = "1", description = "학부모의 경우 <strong>한 명</strong>의 자녀 PK가 필요함")
    private Long studentPk;
}
