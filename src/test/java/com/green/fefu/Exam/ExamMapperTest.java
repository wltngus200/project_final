package com.green.fefu.Exam;

import com.green.fefu.Exam.model.ExamReq;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
@MybatisTest
@ActiveProfiles("tdd")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ExamMapperTest {
    @Autowired
    private ExamMapperTest mapperTest;

    @Test
    void examIns() {
        ExamReq p = new ExamReq();
        p.setSemesterId(8);
        p.setOption(1);
        p.setExamId(1);
        p.setSubjectId(3);


        assertEquals(1, 0);
//        SemesterReq p = new SemesterReq();
//        p.setSemesterId(1);
//        p.setOption(1);
//
//        long res = mapper.postSemester(p);
//        assertEquals(1, res);
//        SemesterReq p1 = new SemesterReq();
//        p1.setSemesterId(2);
//        p1.setOption(2);
//
//        long res1= mapper.postSemester(p1);
//        assertEquals(1, res1);
//
    }
}