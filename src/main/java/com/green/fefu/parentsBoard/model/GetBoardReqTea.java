package com.green.fefu.parentsBoard.model;

import lombok.Getter;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

@Getter
@Setter
public class GetBoardReqTea {
    private long teaId;
    @JsonIgnore
    private int classId;
}
