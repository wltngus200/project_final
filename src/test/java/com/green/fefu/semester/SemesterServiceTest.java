package com.green.fefu.semester;

import com.green.fefu.Subjcet.SubjectServiceImpl;
import com.green.fefu.semester.model.SemesterReq;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class) // spring 컨테이너를 사용하고 싶음 .직접
@Import({SemesterService.class})
class SemesterServiceTest {
    @MockBean
    private SemesterMapper mapper;

    @Autowired
    private SemesterService service;
    @Test
    void postSemester()  {
        SemesterReq p  = new SemesterReq();
        p.setSemesterId(1);
        p.setOption(1);

        given(mapper.postSemester(p)).willReturn(1);
        long res = service.postSemester(p);
        assertEquals(1, res);

        SemesterReq p1  = new SemesterReq();
        p1.setSemesterId(2);
        p1.setOption(2);
        given(mapper.postSemester(p)).willReturn(3);
        long res1 = service.postSemester(p);
        assertEquals(3, res1);
    }
}