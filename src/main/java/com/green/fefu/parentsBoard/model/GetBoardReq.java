package com.green.fefu.parentsBoard.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetBoardReq {
    private String writerType;
    private long writerId;
    private int classId;

    //임시
    private long studentPk;
}
