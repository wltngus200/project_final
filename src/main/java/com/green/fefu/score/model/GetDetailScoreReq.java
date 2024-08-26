package com.green.fefu.score.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@EqualsAndHashCode
@ToString
public class GetDetailScoreReq {
    @JsonIgnore
    private int scoreId;
    @Schema(example = "1", description = "학생Pk")
    private int studentPk;
    @Schema(example = "1", description = "학년")
    private int grade;
    @Schema(example = "1", description = "학기")
    private int semester;
    @JsonIgnore
    private int latestGrade;
    @Schema(example = "1", description = "중간고사 기말고사 ")
    private int exam;
}