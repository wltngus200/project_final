package com.green.fefu.score.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class StuGetRes {

    @JsonIgnore
    private int scoreId;


    private int studentPk;

    @JsonIgnore
    private int latestGrade;
    @JsonIgnore
    private int latestSemester;
    @JsonIgnore
    private String latestYear;

    private int exam;
}
