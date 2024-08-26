package com.green.fefu.online.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OnlineTestRecordListRes {
    private long recodePk;
    private String title;
    private String createdAt;
    private String subject;
}
