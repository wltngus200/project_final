package com.green.fefu.online.model;

import com.green.fefu.entity.HaesolOnlineMultiple;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
        //이건 하나하나의 문제
public class GetKoreanAndMathQuestionRes {
    private long queId;
    // question Entity
    private String question;
    private int level;
    //private int typeTag; // 문법, 독해 구분
    private int queTag; // 주관 객관
    private String contents; // 내용
    //private int answer; // 정답
    private String pic;

    // multiple Entity
    //private List<Integer> num;
    private List<String> sentence=new ArrayList<>(); // 보기 문항



}
