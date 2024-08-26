package com.green.fefu.student.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class getStudent {
    private long studentPk;
    private int num;
    private String name;
    private String gender;
    private String birth;
    private String phone;
    private String parentName;
    private String parentPhone;
}
