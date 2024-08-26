package com.green.fefu.parents.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SignatureReq {
    @JsonIgnore
    private long signId ;

    @Schema(description = "학생 pk")
    private long studentPk;

    @Schema(description = "년도")
    private int year ;

    @Schema(description = "학기")
    private int semester ;
    @JsonIgnore
    private String pic ;
    @Schema(description = "시험 사인")
    private int examSign ;
}
