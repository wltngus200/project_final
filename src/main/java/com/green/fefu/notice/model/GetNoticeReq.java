package com.green.fefu.notice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.BindParam;


@Getter
@Setter
@EqualsAndHashCode
public class GetNoticeReq {
    //@Schema(name="class_id")
    @JsonIgnore
    private long classId;
    @JsonIgnore
    private int state;

    @Schema(example = "3", description = "학부모는 <strong>무조건</strong> 보내야함")
    private long studentPk;
}
