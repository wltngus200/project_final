package com.green.fefu.online.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class PostOnlineQuestionReq {
    // ========== 프론트와 상관 없는 부분 ==========
    @JsonIgnore
    private Long queId;

    @JsonIgnore
    private Long teacherPk;


    // ========== 시험 과목 불러오기 및 분류 ==========
    @Schema(example = "1", description = "[과목 코드] 1: 국어, 2: 수학")
    private Long subjectCode;

    @Schema(example = "11", description = "11->독해 12->문법 13->문학")
    private Long typeTag;

    @Schema(example = "1", description = "1->객관식 2->주관식")
    private Integer queTag=1;

    // ========== 실제 출력 되는 항목 ==========
    @Schema(example="5", description="난이도를 표시하는 항목입니다.")
    private int level;

    @Schema(example = "(TEST)다음 시의 저자로 옳은 사람을 고르시오.", description = "문항을 입력하는 부분입니다.")
    private String question;

    @Schema(example = "(TEST)내가 그의 이름을 불러주었을 때, 그는 나에게로 와서 꽃이 되었다.", description = "내용을 기입하는 부분입니다.")
    private String contents;

    @Schema(example = "[\"(TEST)김춘수\",\"(TEST)윤동주\",\"(TEST)이광수\",\"(TEST)진소월\",\"(TEST)나태주\"]",
                description = "보기 번호 없이 내용만 입력하는 부분입니다.")
    private List<String> multiple=new ArrayList();


    @Schema(example = "1", description = "정답이 되는 보기 번호가 들어가는 부분입니다.")
    private int answer;

    @Schema(example="화자는 봄을 맞이하여 기쁘기 때문에 1번입니다.", description = "문제에 대한 해설이 들어가는 부분입니다.")
    private String explanation;
    // 사진은 별도로 받음

//    @JsonIgnore
//    private Long classId;
}
