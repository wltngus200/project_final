package com.green.fefu.student.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClassInsert {
    private int classPk;
    private long teacherPk;
    private long studentPk;
}
