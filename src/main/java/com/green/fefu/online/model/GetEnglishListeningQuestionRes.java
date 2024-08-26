package com.green.fefu.online.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetEnglishListeningQuestionRes {
    private long queId;
    private String question;
    private String answer;
    private String pic;
    private String sentence;
}
