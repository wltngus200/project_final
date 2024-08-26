package com.green.fefu.score;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class GetScoreReq {
    @JsonIgnore
    private int scoreId;
    @Schema(example = "1", description = "학생Pk")
    private int studentPk;
    @JsonIgnore
    private int latestSemester;
    @JsonIgnore
    private int latestGrade;
    @JsonIgnore
    private int semester;

    @Schema(example = "1", description = "중간고사 기말고사 ")
    private int exam;
}