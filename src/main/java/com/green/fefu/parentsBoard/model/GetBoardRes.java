package com.green.fefu.parentsBoard.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetBoardRes {
    private long boardId;
    private long writerId;
    private String content;
    private String createdAt;
}
