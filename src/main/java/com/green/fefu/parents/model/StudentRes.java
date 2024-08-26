package com.green.fefu.parents.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Data
public class StudentRes {
    @Schema(description = "학생이름")
    private String studentName ;
    @Schema(description = "학생 id")
    private String uid ;
    @Schema(description = "승인여부")
    private int accept ;
    @Schema(description = "재학, 졸업, 퇴학 등등")
    private int state ;
    @Schema(description = "학생 생일")
    private String birth ;
    @Schema(description = "학생 pk")
    private long studentPk ;
    @Schema(description = "학생 학년반번호")
    private String grade ;
    @Schema(description = "학생 사진")
    private String pic ;
    @Schema(description = "학생 고유 코드")
    private String randCode ;

    @Schema(description = "학생 담임선생이름")
    private String teacherName ;
    @Schema(description = "학생 담임선생 전화번호")
    private String teacherPhone ;
    @Schema(description = "학생 담임선생 이메일")
    private String teacherEmail ;
    @Schema(description = "선생 PK")
    private long teaId ;
}
