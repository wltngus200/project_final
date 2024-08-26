package com.green.fefu.online.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostOnlineQuestionEnglishWordReq {
    @JsonIgnore
    private Long wordId;

    @Schema(example = "apple", description = "영단어를 입력하는 부분입니다.")
    private String word;

    @Schema(example = "사과", description = "한국어 해석을 입력하는 부분입니다.")
    private String answer;

    @JsonIgnore
    private Long teacherPk;

}
