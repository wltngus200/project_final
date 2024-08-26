package com.green.fefu.score.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class InsScoreList {
    private String name;

    private int exam;

    private int mark;

    private Integer scoreId;

    private int subjectClassRank;

    private int subjectGradeRank;

    private int studentPk;

    private double classAvg;

    private double gradeAvg;

}
