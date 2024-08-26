package com.green.fefu.student.model.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class getDetail {
    private String pk;
    private String name;
    private String birth;
    private String gender;
    private String phone;
    private String addr;
    private String etc;
    private String createdAt;
    private String parentId;
    private String uClass;
    private String parentName;
    private String connet;
    private String parentPhone;
    private String pic;
    private String teacherName;
}
