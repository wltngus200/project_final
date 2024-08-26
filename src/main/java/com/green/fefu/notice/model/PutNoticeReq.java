package com.green.fefu.notice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class PutNoticeReq {
    @JsonProperty("notice_id")
    private long noticeId;
    @JsonIgnore
    //@JsonProperty("tea_id")
    private long teaId;

    private String title;
    private String content;

}
