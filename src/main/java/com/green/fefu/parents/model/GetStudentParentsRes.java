package com.green.fefu.parents.model;

import lombok.Data;

@Data
public class GetStudentParentsRes {
    private long studentPk ;
    private String name ;
    private String gender ;
    private String birth ;
    private String phone ;
    private String parentName ;
    private String parentPhone ;
    private String parentsPK ;
    private String classId;
    private int age ;
    private String teacherName ;
    private String pic ;
    private long teaId ;
}
