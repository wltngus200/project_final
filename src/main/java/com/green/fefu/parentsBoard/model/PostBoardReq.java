package com.green.fefu.parentsBoard.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostBoardReq {
    private String writerType;
    private long parentsId;
    private String content;
}
