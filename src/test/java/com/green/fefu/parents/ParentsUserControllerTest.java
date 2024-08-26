package com.green.fefu.parents;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.fefu.CharEncodingConfiguration;
import com.green.fefu.parents.model.*;
import com.green.fefu.security.jwt.JwtTokenProviderV2;
import com.green.fefu.sms.SmsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({ParentsUserControllerImpl.class})
@Import({CharEncodingConfiguration.class})
@AutoConfigureMockMvc(addFilters = false)  // Spring Security 필터 비활성화
@WebAppConfiguration
class ParentsUserControllerTest {
    @Autowired private ObjectMapper om;
    @Autowired private MockMvc mockMvc;
    @MockBean private ParentsUserServiceImpl service;
    @MockBean private JwtTokenProviderV2 tokenProvider;
    @MockBean private SmsService smsService;
    private PostParentsUserReq req ;
    private final String BASE_URL = "/api/user/parents";
    @Autowired private ParentsUserControllerImpl controller;
    @BeforeEach
    void setUp() {
        req = new PostParentsUserReq();
//        req.setStudentPk(1);
        req.setNm("길동");
        req.setEmail("rffrfr@gmail.com");
        req.setConnect("부");
        req.setPhone("010-1235-4567");
        req.setUid("parent745");
        req.setUpw("Test1234!@#$");
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }
    @Test @DisplayName("회원가입 TDD")
    void testPostParents() throws Exception {
        // 설정 필요한 필드들 추가
        given(service.postParentsUser(any(PostParentsUserReq.class))).willReturn(1);
        String json = om.writeValueAsString(req);

        // When & Then
        mockMvc.perform(post(BASE_URL + "/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)) // JSON 요청 바디
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        ArgumentCaptor<PostParentsUserReq> captor = ArgumentCaptor.forClass(PostParentsUserReq.class);
        verify(service).postParentsUser(captor.capture());
    }
    @Test @DisplayName("아이디 이메일 중복조회")
    void checkEmailOrUid() throws Exception {
        CheckEmailOrUidReq chReq = new CheckEmailOrUidReq() ;
        chReq.setUid("parent745") ;
        given(service.checkEmailOrUid(chReq)).willReturn("OK") ;
        String json = om.writeValueAsString(chReq) ;

        mockMvc.perform(get(BASE_URL + "/check-duplication")
                        .param("uid", "parent745"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK")) ;

        verify(service).checkEmailOrUid(chReq) ;
        assertEquals("parent745", chReq.getUid()) ;
    }
    @Test @DisplayName("정보조회") @WithMockUser(roles = "PARENTS")
    void getParentsUser() throws Exception {
        String token = "valid-token" ;
        ParentsUserEntity entity = new ParentsUserEntity() ;
        entity.setParentsId(1) ;
        entity.setNm("길동") ;

        given(tokenProvider.resolveToken(any(HttpServletRequest.class))).willReturn(token) ;
        given(service.getParentsUser(token)).willReturn(entity) ;

        mockMvc.perform(get(BASE_URL + "/parent-info")
                .header("Authorization", "Bearer" + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.parentsId").value(1))
                .andExpect(jsonPath("$.nm").value("길동")) ;
        verify(tokenProvider).resolveToken(any(HttpServletRequest.class)) ;
        verify(service).getParentsUser(token) ;
    }
    @Test @DisplayName("정보조회 토큰없음") @WithMockUser(roles = "PARENTS")
    void getParentsUser2() throws Exception {
        given(tokenProvider.resolveToken(any(HttpServletRequest.class))).willReturn(null);

        mockMvc.perform(get(BASE_URL + "/parent-info"))
                .andExpect(status().isUnauthorized());

        verify(tokenProvider).resolveToken(any(HttpServletRequest.class));
        verify(service, never()).getParentsUser(anyString());
    }
    @Test @DisplayName("정보수정") @WithMockUser(roles = "PARENTS")
    void patchParentsUser() throws Exception{
        PatchParentsUserReq p = new PatchParentsUserReq() ;
        p.setParentsId(req.getParentsId()) ;
        p.setNm("홍홍") ;

        given(service.patchParentsUser(any(PatchParentsUserReq.class))).willReturn(1) ;
        String json = om.writeValueAsString(p) ;

        mockMvc.perform(put(BASE_URL + "/info-update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
        verify(service).patchParentsUser(any(PatchParentsUserReq.class));
    }
    @Test @DisplayName("아이디 찾기")
    void getFindId() throws Exception {
        GetFindIdReq getIdReq = new GetFindIdReq() ;
        getIdReq.setNm(req.getNm()) ;
        getIdReq.setPhone(req.getPhone()) ;

        GetFindIdRes res = new GetFindIdRes() ;
        res.setUid(req.getUid()) ;

        given(service.getFindId(getIdReq)).willReturn(res) ;
        String json = om.writeValueAsString(res) ;

        mockMvc.perform(get(BASE_URL + "/find-id")
                .param("nm", req.getNm())
                .param("phone", req.getPhone()))
                .andExpect(status().isOk())
                .andExpect(content().string(json)) ;

        verify(service).getFindId(getIdReq) ;
        assertEquals(req.getUid(), res.getUid()) ;
    }
    @Test @DisplayName("비밀번호 수정")
    void patchPassword() throws Exception {
        PatchPasswordReq req = new PatchPasswordReq();
        req.setParentsId(1L);
        req.setNewUpw("newPassword123");

        given(service.patchPassword(any(PatchPasswordReq.class))).willReturn(1);
        String json = om.writeValueAsString(req);

        // When & Then
        mockMvc.perform(put(BASE_URL + "/password-update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        verify(service).patchPassword(any(PatchPasswordReq.class));
    }
    @Test @DisplayName("로그인")
    void signInPost() throws Exception {
        // Given
        SignInPostReq signReq = new SignInPostReq();
        signReq.setUid(req.getUid());
        signReq.setUpw(req.getUpw());
        System.out.println(signReq);

        SignInPostRes res = new SignInPostRes();
        res.setAccessToken("mockToken");
        res.setParentsId(req.getParentsId());
        res.setNm(req.getNm()) ;
        System.out.println(res);

        given(service.signInPost(any(SignInPostReq.class), any(HttpServletResponse.class))).willReturn(res);
        String json = om.writeValueAsString(signReq);

        String expectedJson = om.writeValueAsString(res);

        mockMvc.perform(post(BASE_URL + "/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        verify(service).signInPost(any(SignInPostReq.class), any(HttpServletResponse.class)) ;
    }
    @Test @DisplayName("access-token 재발급") @WithMockUser(roles = "PARENTS")
    void testGetAccessToken() throws Exception {
        Map<String, Object> res = new HashMap<>();
        when(service.getAccessToken(any(HttpServletRequest.class))).thenReturn(res);

        mockMvc.perform(get(BASE_URL+ "/access-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap());
    }
    @Test @DisplayName("비밀번호 찾기")
    void testGetFindPassword() throws Exception {
        GetFindPasswordReq req = new GetFindPasswordReq();
        Map<String, Object> res = new HashMap<>();

        doAnswer(invocation -> {
            ((Map) invocation.getArguments()[1]).putAll(res);
            return null;
        }).when(service).getFindPassword(any(GetFindPasswordReq.class), any(Map.class));

        mockMvc.perform(get(BASE_URL + "/find-password")
                        .param("stuId", "1")
                        .param("year", "2024")
                        .param("semester", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap());
    }
    @Test @DisplayName("자녀정보조회") @WithMockUser(roles = "PARENTS")
    void testGetStudentParents() throws Exception {
        String token = "valid-token" ;
        given(tokenProvider.resolveToken(any(HttpServletRequest.class))).willReturn(token) ;

        List<GetStudentParentsRes> resList = new ArrayList<>();
        when(service.getStudentParents(any(String.class))).thenReturn(resList);

        mockMvc.perform(get(BASE_URL + "/get-student-parent")
                        .header("Authorization", "Bearer" + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}