package com.green.fefu.student.service;

import com.green.fefu.student.model.dto.*;
import com.green.fefu.student.model.req.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StudentMapper {
    int createStudent(createStudentReq p);

    int deleteStudent(deleteStudentReq p);

    List<getStudent> getStudentList(long pk);

    getDetail getStudentDetail(long pk);

    int updateStudent(updateStudentReq p);

    List<getListForNoParent> getStudentListForParent(String searchWord);

    int updStudentGrade(studentAdvanceGradeReq p);

    int insNewClass(studentAdvanceGradeReq p);

    getUserTest selOneTest(long pk);

    String getStudentEtc(long pk);

    void updStudentEtc(long pk, String etc);

    List<prevStudentEtc> selPrevEtc(long pk);

    int updStudentParent(String studentRandCode, long parentPk);

    void insertClassIfNotExists(ClassInsert p);

    void insertClassStudent(ClassInsert p);

    String findTeacherName(Long studentPk);
}
