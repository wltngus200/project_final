package com.green.fefu.Subjcet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class SubjectReq {
    @JsonIgnore
    private long subjectId;

    @Schema(description = " 국어 " ,defaultValue = "과목이름")

    private String subjectName;
    @Schema(description = "1",defaultValue = "학년 ")

    private long grade;
}
