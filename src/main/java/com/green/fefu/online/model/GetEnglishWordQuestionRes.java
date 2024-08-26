package com.green.fefu.online.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetEnglishWordQuestionRes {
    private Long wordQuePk;
    private String word;
    private String answer;
    private String pic;
}
