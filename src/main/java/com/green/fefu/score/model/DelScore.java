package com.green.fefu.score.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class DelScore {
    @JsonIgnore
    private long scoreId;

    private long studentPk;

    private int semester;

    private int exam;

    private String name;

    private int year;


}
