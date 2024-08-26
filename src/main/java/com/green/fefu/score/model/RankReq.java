package com.green.fefu.score.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class RankReq {
    @JsonIgnore
    private int studentPk;
    @JsonIgnore
    private int grade;
    @JsonIgnore
    private int semester;
    @JsonIgnore
    private int exam;

}
