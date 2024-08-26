package com.green.fefu.teacher.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TeacherEntity {
    private long pk;
    private String id;
    private String password;
    private String name;
    private String phone;
    private String email;
    private String gender;
    private String birth;
    private String auth;
    private String addr;
    private String acept;
    private String createdAt;
    private String updatedAt;
}
