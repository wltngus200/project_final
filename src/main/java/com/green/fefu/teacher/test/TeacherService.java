package com.green.fefu.teacher.test;

import com.green.fefu.entity.Teacher;
import com.green.fefu.teacher.model.req.*;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public interface TeacherService {
    Map CreateTeacher(CreateTeacherReq p, Map map);

    Map LogInTeacher(LogInTeacherReq p, Map map, HttpServletResponse res);

    void CheckDuplicate(CheckDuplicateReq p);

    Map FindTeacherId(FindTeacherIdReq p, Map map);

    void FindTeacherPassword(FindTeacherPasswordReq p, Map map);

    Teacher ChangePassWord(ChangePassWordReq p);

    Map TeacherDetail(Map map);

    void ChangeTeacher(ChangeTeacherReq p);
}
