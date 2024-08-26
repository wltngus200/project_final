package com.green.fefu.socket.model;

import com.green.fefu.entity.Teacher;
import lombok.Data;

@Data
public class TeacherDto {
    private Long teaId;

    private String name;


    private int grade;

    private int classNumber;

    public TeacherDto(Teacher data) {
        this.teaId = data.getTeaId() ;
        this.name = data.getName() ;
    }
}
