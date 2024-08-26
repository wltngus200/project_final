package com.green.fefu.notice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.bind.annotation.BindParam;

import java.beans.ConstructorProperties;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DeleteNoticeReq {
    @Schema(name="notice_id")
    private long noticeId;
    @JsonIgnore
    //@Schema(name="tea_id")
    private long teaId;
    @JsonIgnore
    //@Schema(name="class_id")
    private int classId;

    public DeleteNoticeReq(@BindParam("notice_id")long noticeId){
        this.noticeId=noticeId;
    }
}
