package com.green.fefu.notice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class GetNoticeRes {
    @JsonProperty("notice_id")
    private long noticeId;
    private long teaId;
    private long classId;
    private String title;
    private String content;
    private String createdAt;
    private String updatedAt;
    private int state;
}
