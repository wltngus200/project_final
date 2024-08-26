package com.green.fefu.common.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class CommentPaging extends Paging {
    private long boardId;

//    @BindParam("board_id") long boardId
    public CommentPaging(Integer page, Integer size, long boardId) {
        super(page, size);

        this.boardId = boardId;
    }
}