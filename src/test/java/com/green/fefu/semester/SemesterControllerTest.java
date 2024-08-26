package com.green.fefu.semester;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.fefu.CharEncodingConfiguration;

import com.green.fefu.Subjcet.SubjectControllerImpl;
import com.green.fefu.Subjcet.SubjectService;
import com.green.fefu.common.ResultDto;
import com.green.fefu.security.SecurityConfiguration;
import com.green.fefu.security.jwt.JwtAuthenticationFilter;
import com.green.fefu.semester.model.SemesterReq;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({CharEncodingConfiguration.class, SecurityConfiguration.class})
@WebMvcTest(SemesterControllerImpl.class)
class SemesterControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private SemesterService service;
    @Autowired
    private ObjectMapper om;
    @MockBean
    private JwtAuthenticationFilter filter;

    @Autowired
    private WebApplicationContext wac;

    private  final String BASE_URL = "/api/semester";



    @BeforeEach
        public void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();
    }

    @Test
    void postSemester() throws Exception {

        SemesterReq p = new SemesterReq();
        p.setSemesterId(1);
        p.setOption(1);
        int res = 1;

        given(service.postSemester(p)).willReturn(1);

        String json = om.writeValueAsString(p);
        System.out.println(p);

        System.out.println(given(service.postSemester(p)).willReturn(1));

        mvc.perform(
                        post(BASE_URL+"/post")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                ).andExpect(status().isOk()) // 200 으로 넘어오는지 확인
                .andExpect(content().string(String.valueOf(1))) // 실제로 넘어온 값
                .andDo(print());

        verify(service).postSemester(any(SemesterReq.class));
    }
}