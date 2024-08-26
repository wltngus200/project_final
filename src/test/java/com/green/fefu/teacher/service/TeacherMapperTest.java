package com.green.fefu.teacher.service;

import com.green.fefu.teacher.model.dto.EntityArgument;
import com.green.fefu.teacher.model.dto.TeacherEntity;
import com.green.fefu.teacher.model.req.ChangePassWordReq;
import com.green.fefu.teacher.model.req.ChangeTeacherReq;
import com.green.fefu.teacher.model.req.CreateTeacherReq;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TeacherMapperTest {
    @Autowired
    private TeacherMapper mapper;

    @Test
    void createTeacher() {
        CreateTeacherReq p = new CreateTeacherReq();
        p.setTeacherId("123");
        p.setPassword("123");
        p.setName("123");
        p.setPhone("010-0000-0000");
        p.setEmail("123@qq.com");
        p.setGender("남");
        int result = mapper.CreateTeacher(p);
        assertEquals(1, result);
        assertNotNull(p.getTeacherPk());
    }

    @Test
    void getTeacher() {
        EntityArgument p = EntityArgument.builder()
                .pk(1L)
                .build();
        TeacherEntity result = mapper.GetTeacher(p);
        assertNotNull(result);
        assertEquals(1L, result.getPk());
    }

    @Test
    void getCurrentClassesByTeacherId() {
        String result = mapper.getCurrentClassesByTeacherId(1L);
        assertEquals("101", result);
    }

    @Test
    void changePassWord() {
        ChangePassWordReq req = new ChangePassWordReq();
        req.setPk(2L);
        req.setPassWord("newPassword");

        int updatedRows = mapper.ChangePassWord(req);
        assertEquals(1, updatedRows, "Password should be updated successfully");
    }

    @Test
    void changeTeacher() {
        ChangeTeacherReq req = new ChangeTeacherReq();
        req.setPk(2L);
        req.setName("Updated");
        req.setPhone("010-1239-5678");
        req.setEmail("updated123@example.com");
        req.setFullAddr("123 # Address # 123");

        int updatedRows = mapper.ChangeTeacher(req);
        assertEquals(1, updatedRows, "Teacher details should be updated successfully");
    }
}