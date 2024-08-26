package com.green.fefu.Subjcet;

import com.green.fefu.Subjcet.model.SubjectReq;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class) // spring 컨테이너를 사용하고 싶음 .직접
@Import({SubjectServiceImpl.class})
class SubjectServiceTest {
//    @MockBean
//    private SubjectMapper mapper;
//
//    @Autowired
//    private SubjectService service;
//
//    @Test
//    void postSubject() {
//        SubjectReq p = new SubjectReq(1,"국어",1);
//
//        given(mapper.SubjectReq(p)).willReturn(1);
//
//        int res= service.postSubject(p);
//
//        assertEquals(1,res);
//        verify(mapper).SubjectReq(p);
//
//        SubjectReq p1 = new SubjectReq(2,"수학",1);
//        given(mapper.SubjectReq(p1)).willReturn(3);
//
//        assertEquals(3,service.postSubject(p1));
//        verify(mapper).SubjectReq(p1);
//    }
}