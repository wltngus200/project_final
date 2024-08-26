package com.green.fefu.online.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddTagInfoReq {
    private long subject;
    private int typeNum;
    private String tagName;
}
