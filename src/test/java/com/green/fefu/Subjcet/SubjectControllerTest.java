package com.green.fefu.Subjcet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.fefu.CharEncodingConfiguration;
import com.green.fefu.Subjcet.model.SubjectReq;
import com.green.fefu.common.ResultDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(CharEncodingConfiguration.class)
@WebMvcTest(SubjectControllerImpl.class)

class SubjectControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private SubjectService service;
    @Autowired
    private ObjectMapper om;
    private  final String BASE_URL = "/api/Subject";

//    @Test
//    void postSubject() throws Exception {
//        SubjectReq p = new SubjectReq(1,"국어",1);
//        int res = 0;
//        given(service.postSubject(p)).willReturn(res);
//        String reqJson = om.writeValueAsString(p);
//
//        ResultDto<Integer> expectedResult =  ResultDto.<Integer>builder()
//                .statusCode(HttpStatus.OK)
//                .resultMsg(HttpStatus.OK.toString())
//                .resultData(res)
//                .build();
//
//        String expectedJson = om.writeValueAsString(expectedResult);
//        mvc.perform(
//                        post(BASE_URL)
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(reqJson)
//                )
//                .andExpect(status().isOk())
//                .andExpect(content().json(expectedJson))
//                .andDo(print());
//        verify(service).postSubject(p);
//    }
}