package com.green.fefu.Exam.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@EqualsAndHashCode
public class ExamReq {

    @JsonIgnore
    private int examId;

    private int semesterId;
    private int subjectId;
    private int option;


}
