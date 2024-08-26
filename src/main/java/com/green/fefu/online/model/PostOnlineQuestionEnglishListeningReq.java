package com.green.fefu.online.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostOnlineQuestionEnglishListeningReq {
    @JsonIgnore
    private long ListeningPk;

    @Schema(example = "Daniel이 좋아하는 한국 음식은 무엇입니까?", description = "문제를 입력하는 부분입니다.")
    private String question;

    @Schema(example = "kimchi", description = "정답을 입력하는 부분입니다.")
    private String answer;

    @Schema(example = "Hi, I'm Daniel. I'm from USA. I like korean food. My favorite korean food is kimchi", description = "보이스웨어가 읽을 문장을 입력하는 부분입니다.")
    private String sentence;

}
