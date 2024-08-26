package com.green.fefu.semester;

import com.green.fefu.semester.model.SemesterReq;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
@MybatisTest
@ActiveProfiles("tdd")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SemesterMapperTest {
    @Autowired
    private SemesterMapper mapper;

    @Test
    void postSemester() {
        SemesterReq p = new SemesterReq();
        p.setSemesterId(1);
        p.setOption(1);

        long res = mapper.postSemester(p);
        assertEquals(1, res);
        SemesterReq p1 = new SemesterReq();
        p1.setSemesterId(2);
        p1.setOption(2);

        long res1= mapper.postSemester(p1);
        assertEquals(1, res1);
    }
}