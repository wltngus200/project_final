package com.green.fefu.Subjcet;

import com.green.fefu.Subjcet.model.SubjectReq;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@ActiveProfiles("tdd")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SubjectMapperTest {

//    @Autowired private SubjectMapper mapper;


//    @Test
//    void SubjectReq() {
////        SubjectReq p = new SubjectReq(1,"국어",1);
////        int res1 = mapper.SubjectReq(p);
////        assertEquals(1,res1);
////
////        SubjectReq p2 = new SubjectReq(3,"수학",2);
////
////        int res2 = mapper.SubjectReq(p2);
////        assertEquals(1,res2);
////    }
//
}