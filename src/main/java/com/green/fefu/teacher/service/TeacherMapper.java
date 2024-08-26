package com.green.fefu.teacher.service;

import com.green.fefu.teacher.model.dto.EntityArgument;
import com.green.fefu.teacher.model.dto.TeacherEntity;
import com.green.fefu.teacher.model.req.ChangePassWordReq;
import com.green.fefu.teacher.model.req.ChangeTeacherReq;
import com.green.fefu.teacher.model.req.CreateTeacherReq;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TeacherMapper {
    int CreateTeacher(CreateTeacherReq p);

    TeacherEntity GetTeacher(EntityArgument p);

    String getCurrentClassesByTeacherId(long teacherPk);

    int ChangePassWord(ChangePassWordReq p);

    int ChangeTeacher(ChangeTeacherReq p);
}
