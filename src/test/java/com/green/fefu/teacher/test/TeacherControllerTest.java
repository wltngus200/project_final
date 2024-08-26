package com.green.fefu.teacher.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.fefu.CharEncodingConfiguration;
import com.green.fefu.security.jwt.JwtTokenProviderV2;
import com.green.fefu.teacher.model.req.*;
import com.green.fefu.teacher.service.TeacherControllerImpl;
import com.green.fefu.teacher.service.TeacherServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TeacherControllerImpl.class)

@Import(CharEncodingConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
@WebAppConfiguration
class TeacherControllerTest {

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TeacherServiceImpl service;
    @MockBean
    private JwtTokenProviderV2 tokenProvider;

    private Map map = new HashMap();

    private CreateTeacherReq createReq;
    private final String BASE_URL = "/api/teacher";

    @BeforeEach
    void setUp() {
        createReq = new CreateTeacherReq();
        createReq.setName("홍길동");
        createReq.setTeacherId("123teacher");
        createReq.setPassword("123password!@#");
        createReq.setPhone("010-0000-0000");
        createReq.setEmail("test@gmail.com");
        createReq.setGender("남");
        createReq.setBirth("2024-06-28");
        createReq.setZoneCode("12345");
        createReq.setAddr("서울 판교 1234");
        createReq.setDetail("101동");
    }

    @Test
    @DisplayName("회원가입 TDD")
    void createTeacher() throws Exception {
        map.put("teacherPk", 1);
        // given에서 Map을 반환하도록 설정합니다.
        given(service.CreateTeacher(any(CreateTeacherReq.class), any())).willReturn(map);

        String json = mapper.writeValueAsString(createReq);

        mockMvc.perform(post(BASE_URL + "/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teacherPk").value(1)); // 여기서는 예상 응답 값으로 "1"을 기대하고 있지만, Map을 반환하는 메서드에 따라 다를 수 있습니다.

        ArgumentCaptor<CreateTeacherReq> captor = ArgumentCaptor.forClass(CreateTeacherReq.class);
        verify(service).CreateTeacher(captor.capture(), any());
        assertEquals("123teacher", captor.getValue().getTeacherId()); // createReq의 teacherId 값을 확인합니다.
    }
//    @Test
//    @DisplayName("로그인 TDD")
//    void logInTeacher() throws Exception {
//        LogInTeacherReq logInReq = new LogInTeacherReq();
//        logInReq.setTeacherId("teacher123");
//        logInReq.setPassword("password123");
//        map.put("name", "");
//        map.put("email", "");
//        map.put("class", "");
//        map.put("accessToken", "");
//        logInRes.setName("홍길동");
//        logInRes.setEmail("teacher@school.com");
//        logInRes.setAccessToken("mockToken");
//
//        given(service.LogInTeacher(any(LogInTeacherReq.class), any(HttpServletResponse.class))).willReturn(logInRes);
//
//        String json = mapper.writeValueAsString(logInReq);
//        String expectedJson = mapper.writeValueAsString(logInRes);
//
//        mockMvc.perform(post(BASE_URL + "/sign-in")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isOk())
//                .andExpect(content().json(expectedJson));
//
//        verify(service).logInTeacher(any(LogInTeacherReq.class), any(HttpServletResponse.class));
//    }
//
//    @Test
//    @DisplayName("아이디 중복 체크 TDD")
//    void checkDuplicate() throws Exception {
//        CheckDuplicateReq checkReq = new CheckDuplicateReq();
//        checkReq.setId("teacher123");
//
//        given(service.checkDuplicate(any(CheckDuplicateReq.class))).willReturn("OK");
//
//        mockMvc.perform(get(BASE_URL + "/check-duplication")
//                        .param("id", "teacher123"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("OK"));
//
//        verify(service).checkDuplicate(any(CheckDuplicateReq.class));
//    }
//
//    @Test
//    @DisplayName("교사 ID 찾기 TDD")
//    void findTeacherId() throws Exception {
//        FindTeacherIdReq findReq = new FindTeacherIdReq();
//        findReq.setName("홍길동");
//
//        FindTeacherIdRes findRes = new FindTeacherIdRes();
//        findRes.setId("teacher123");
//
//        given(service.findTeacherId(any(FindTeacherIdReq.class))).willReturn(findRes);
//
//        mockMvc.perform(get(BASE_URL + "/find-id")
//                        .param("name", "홍길동"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value("teacher123"));
//
//        verify(service).findTeacherId(any(FindTeacherIdReq.class));
//    }
//
//    @Test
//    @DisplayName("교사 비밀번호 찾기 TDD")
//    void findTeacherPassword() throws Exception {
//        FindTeacherPasswordReq findReq = new FindTeacherPasswordReq();
//        findReq.setEmail("teacher@school.com");
//
//        FindTeacherPasswordRes findRes = new FindTeacherPasswordRes();
//        findRes.setRandomCode("123456");
//
//        given(service.findTeacherPassword(any(FindTeacherPasswordReq.class))).willReturn(findRes);
//
//        mockMvc.perform(get(BASE_URL + "/find-password")
//                        .param("email", "teacher@school.com"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.randomCode").value("123456"));
//
//        verify(service).findTeacherPassword(any(FindTeacherPasswordReq.class));
//    }
//
//    @Test
//    @DisplayName("비밀번호 변경 TDD")
//    void changePassWord() throws Exception {
//        ChangePassWordReq changeReq = new ChangePassWordReq();
//        changeReq.setPk("1");
//        changeReq.setPassWord("newPassword123");
//
//        given(service.changePassWord(any(ChangePassWordReq.class))).willReturn(1);
//
//        String json = mapper.writeValueAsString(changeReq);
//
//        mockMvc.perform(put(BASE_URL + "/change-password")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isOk())
//                .andExpect(content().string("1"));
//
//        verify(service).changePassWord(any(ChangePassWordReq.class));
//    }
//
//    @Test
//    @DisplayName("교사 상세 정보 조회 TDD")
//    void teacherDetail() throws Exception {
//        String token = "valid-token";
//        TeacherDetailRes detailRes = new TeacherDetailRes();
//        detailRes.setId("teacher123");
//        detailRes.setName("홍길동");
//
//        given(tokenProvider.resolveToken(any(HttpServletRequest.class))).willReturn(token);
//        given(service.teacherDetail(token)).willReturn(detailRes);
//
//        mockMvc.perform(get(BASE_URL + "/detail")
//                        .header("Authorization", "Bearer " + token))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value("teacher123"))
//                .andExpect(jsonPath("$.name").value("홍길동"));
//
//        verify(tokenProvider).resolveToken(any(HttpServletRequest.class));
//        verify(service).teacherDetail(token);
//    }
//
//    @Test
//    @DisplayName("교사 정보 수정 TDD")
//    void changeTeacher() throws Exception {
//        ChangeTeacherReq changeReq = new ChangeTeacherReq();
//        changeReq.setPk("1");
//        changeReq.setName("홍길동");
//
//        given(service.changeTeacher(any(ChangeTeacherReq.class))).willReturn(1);
//
//        String json = mapper.writeValueAsString(changeReq);
//
//        mockMvc.perform(patch(BASE_URL + "/change-info")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isOk())
//                .andExpect(content().string("1"));
//
//        verify(service).changeTeacher(any(ChangeTeacherReq.class));
//    }

}
