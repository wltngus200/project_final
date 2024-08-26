package com.green.fefu.parents;

import com.green.fefu.chcommon.SmsSender;
import com.green.fefu.common.AppProperties;
import com.green.fefu.common.CookieUtils;
import com.green.fefu.common.CustomFileUtils;
import com.green.fefu.parents.model.*;
import com.green.fefu.parents.model.ParentsUserEntity;
import com.green.fefu.parents.model.PostParentsUserReq;
import com.green.fefu.security.AuthenticationFacade;
import com.green.fefu.security.MyUser;
import com.green.fefu.security.MyUserDetails;
import com.green.fefu.security.jwt.JwtTokenProviderV2;
import com.green.fefu.sms.SmsService;
import com.green.fefu.student.service.StudentMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@TestPropertySource(
        properties = {
                "file.dir=D:\\download\\2nd"
        }
)
@ExtendWith(SpringExtension.class)
@Import({ParentsUserServiceImpl.class})
class ParentsUserServiceTest {
    @Value("${file.dir}") String uploadPath;
    @MockBean private ParentsUserMapper mapper;
    @Autowired private ParentsUserService service;
    @MockBean private CustomFileUtils customFileUtils;
    @MockBean private PasswordEncoder passwordEncoder;
    @MockBean private JwtTokenProviderV2 jwtTokenProvider;
    @MockBean private CookieUtils cookieUtils;
    @MockBean private AuthenticationFacade authenticationFacade;
    @MockBean private Authentication authentication ;
    @MockBean private AppProperties appProperties;
    @MockBean private SmsService smsService ;
    @MockBean private StudentMapper studentMapper;
    @MockBean private HttpServletRequest httpServletRequest;
    @Value("${coolsms.api.caller}") private String coolsmsApiCaller;

    private PatchPasswordReq validGetFindPasswordReq;
    private Cookie refreshTokenCookie ;
    private MockHttpServletRequest req;
    private MockHttpServletResponse res;
    private Map<String, Object> map;
    private PatchPasswordReq patchPasswordReq;
    private ParentsUserEntity parentsUserEntity;
    private GetFindPasswordReq getReq;
    private MultipartFile validFile;
    private MultipartFile emptyFile;
    private SignatureReq signReq;
    @BeforeEach
    void setUp() {
        validGetFindPasswordReq = new PatchPasswordReq();
        validGetFindPasswordReq.setUid("1");
        validGetFindPasswordReq.setParentsId(1);
        validGetFindPasswordReq.setNewUpw("newPassword");
        MockitoAnnotations.openMocks(this);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        given(securityContext.getAuthentication()).willReturn(authentication);

        MyUserDetails myUserDetails = mock(MyUserDetails.class);
        MyUser myUser = new MyUser();
        myUser.setUserId(1L);
        given(authentication.getPrincipal()).willReturn(myUserDetails);
        given(myUserDetails.getMyUser()).willReturn(myUser);
        validFile = new MockMultipartFile("pic", "a.png", "image/png", new byte[]{1, 2, 3, 4});
        emptyFile = new MockMultipartFile("pic", "a.png", "image/png", new byte[]{});
        signReq = SignatureReq.builder()
                .studentPk(1)
                .year("2024")
                .pic(null)
                .semester(1)
                .signId(1)
                .build();
        getReq = new GetFindPasswordReq();
        getReq.setPhone("010-1234-5678");
        map = new HashMap<>();

        patchPasswordReq = new PatchPasswordReq();
        patchPasswordReq.setNewUpw("newPassword");

        req = new MockHttpServletRequest();
        res = new MockHttpServletResponse();
        map = new HashMap<>();
        getReq = new GetFindPasswordReq();
        getReq.setPhone("010-1234-5678");

        parentsUserEntity = new ParentsUserEntity();
        parentsUserEntity.setParentsId(1L);
        refreshTokenCookie = new Cookie("refresh-token", "dummy-refresh-token");
    }
    @Test @DisplayName("post 1") // 회원가입
    void postParentsUser() {
        PostParentsUserReq p1 = new PostParentsUserReq();
        p1.setUid("pG123456");
        p1.setUpw("aAbB!@1212");
        p1.setNm("홍길동");
        p1.setEmail("12345@naver.com");
        p1.setPhone("010-1234-1234");
        p1.setConnect("부");
        p1.setStudentPk(1);

        PostParentsUserReq p2 = new PostParentsUserReq();
        p2.setUid("pG234567");
        p2.setUpw("aAbB!@1212");
        p2.setNm("김길동");
        p1.setEmail("12345678@naver.com");
        p2.setPhone("010-2345-2345");
        p2.setConnect("모");
        p2.setStudentPk(2);

        given(studentMapper.updStudentParent(p1.getStudentPk(), p1.getParentsId())).willReturn(1) ;
        given(studentMapper.updStudentParent(p2.getStudentPk(), p2.getParentsId())).willReturn(1) ;

        given(mapper.postParentsUser(p1)).willReturn(1);
        given(mapper.postParentsUser(p2)).willReturn(1);

        assertEquals(1, service.postParentsUser(p1),"1. 이상");
        assertEquals(1, service.postParentsUser(p2),"2. 이상");

        verify(mapper).postParentsUser(p1);
        verify(mapper).postParentsUser(p2);
    }
    @Test @DisplayName("회원정보 확인") // 회원정보 확인
    void getParentsUser() {
        HttpServletResponse res = null ;
        long parentsId = 100 ;
        PostParentsUserReq p1 = new PostParentsUserReq();
        p1.setUid("pG123456");
        p1.setUpw("aAbB!@1212");
        p1.setNm("홍길동");
        p1.setEmail("12345@naver.com");
        p1.setPhone("010-1234-1234");
        p1.setConnect("부");
        p1.setParentsId(parentsId) ;

        String token = "accessToken" ;
        Authentication auth = mock(Authentication.class) ;
        MyUserDetails myUserDetails = mock(MyUserDetails.class) ;
        MyUser myUser = new MyUser() ;
        myUser.setUserId(p1.getParentsId());

        given(jwtTokenProvider.getAuthentication(token)).willReturn(auth) ;
        given(auth.getPrincipal()).willReturn(myUserDetails) ;
        given(myUserDetails.getMyUser()).willReturn(myUser) ;

        GetParentsUserReq req = new GetParentsUserReq() ;
        req.setSignedUserId(p1.getParentsId()) ;
        ParentsUserEntity entity = new ParentsUserEntity() ;
        entity.setParentsId(p1.getParentsId()) ;
        entity.setUid(p1.getUid());
        entity.setUpw(p1.getUpw());
        entity.setNm(p1.getNm());
        entity.setEmail(p1.getEmail());
        entity.setPhone(p1.getPhone());
        entity.setConnet(p1.getConnect());

        System.out.println("Request: " + req) ;
        System.out.println("Entity: " + entity);

        given(mapper.getParentsUser(eq(req))).willReturn(entity) ;

        ParentsUserEntity res0 = service.getParentsUser(token) ;

        System.out.println("ResultEntity: " + res0) ;

        assertEquals(entity, res0) ;
        verify(jwtTokenProvider).getAuthentication(token) ;
        verify(mapper).getParentsUser(eq(req)) ;

        long parentsId2 = 200L;
        String token2 = "accessToken2";

        PostParentsUserReq p2 = new PostParentsUserReq();
        p2.setUid("pG234567");
        p2.setUpw("aAbB!@1212");
        p2.setNm("김길동");
        p2.setEmail("12345678@naver.com");
        p2.setPhone("010-2345-2345");
        p2.setConnect("모");
        p2.setParentsId(parentsId2);

        Authentication auth2 = mock(Authentication.class);
        MyUserDetails myUserDetails2 = mock(MyUserDetails.class);
        MyUser myUser2 = new MyUser();
        myUser2.setUserId(parentsId2);

        given(jwtTokenProvider.getAuthentication(token2)).willReturn(auth2);
        given(auth2.getPrincipal()).willReturn(myUserDetails2);
        given(myUserDetails2.getMyUser()).willReturn(myUser2);

        GetParentsUserReq req2 = new GetParentsUserReq();
        req2.setSignedUserId(parentsId2);

        given(mapper.getParentsUser(eq(req2))).willReturn(null);

        ParentsUserEntity res2 = service.getParentsUser(token2);

        assertNull(res2);
        verify(jwtTokenProvider).getAuthentication(token2);
        verify(mapper).getParentsUser(eq(req2));

    }
    @Test @DisplayName("정보수정") // 회원정보 수정
    void patchParentsUser() {
        PostParentsUserReq p1 = new PostParentsUserReq();
        p1.setUid("p1");
        p1.setNm("홍길동");
        p1.setPhone("010-1234-1234");
        p1.setConnect("부");
        p1.setParentsId(1L);
        ParentsUserEntity entity = new ParentsUserEntity();
        entity.setParentsId(p1.getParentsId());
        PatchParentsUserReq req = new PatchParentsUserReq();
        req.setParentsId(entity.getParentsId());
        req.setAddr("대구");
        given(mapper.patchParentsUser(req)).willReturn(1);
        assertNotEquals(p1.getAddr(), req.getAddr());
        int result = service.patchParentsUser(req);
        assertEquals(1, result);
        verify(mapper).patchParentsUser(req);

        ParentsUserEntity entity1 = new ParentsUserEntity();
        entity1.setParentsId(p1.getParentsId());
        PatchParentsUserReq req1 = new PatchParentsUserReq();
        req1.setParentsId(entity1.getParentsId());
        req1.setConnet("모");
        req1.setNm("유관순");
        given(mapper.patchParentsUser(req1)).willReturn(2);
        int result1 = service.patchParentsUser(req1);
        assertEquals(2, result1);

        ParentsUserEntity entity2 = new ParentsUserEntity();
        entity2.setParentsId(2L);
        PatchParentsUserReq req2 = new PatchParentsUserReq();
        req2.setParentsId(entity2.getParentsId());
        req2.setConnet("기타");
        given(mapper.patchParentsUser(req2)).willReturn(0);
        int result2 = service.patchParentsUser(req2);
        assertEquals(0, result2);
    }
    @Test @DisplayName("아이디 찾기") // 아이디 찾기
    void getFindId() {
        PostParentsUserReq p1 = new PostParentsUserReq();
        p1.setUid("p1");
        p1.setNm("홍길동");
        p1.setPhone("010-1234-1234");
        p1.setConnect("부");
        p1.setParentsId(1L);

        ParentsUserEntity entity = new ParentsUserEntity();
        entity.setParentsId(p1.getParentsId());

        GetFindIdReq req = new GetFindIdReq();
        req.setPhone("010-1234-1234");
        req.setNm("홍길동");

        GetFindIdRes beforeRes = new GetFindIdRes();
        beforeRes.setUid(entity.getUid());

        given(mapper.getFindId(req)).willReturn(beforeRes);
        GetFindIdRes afterRes = service.getFindId(req);

        assertEquals(beforeRes.getUid(), afterRes.getUid());
        verify(mapper).getFindId(req);
    }
    @Test @DisplayName("비밀번호 수정") // 비밀번호 수정
    void patchPassword() {
        PatchPasswordReq patchPasswordReq = new PatchPasswordReq();
        patchPasswordReq.setParentsId(1L);
        patchPasswordReq.setUid("user123");
        patchPasswordReq.setNewUpw("newPassword");

        ParentsUserEntity parentsUserEntity = new ParentsUserEntity();
        parentsUserEntity.setParentsId(1L);

        List<ParentsUserEntity> entityList = new ArrayList<>();
        entityList.add(parentsUserEntity);

        when(authenticationFacade.getLoginUserId()).thenReturn(1L);
        when(mapper.selPasswordBeforeLogin(any(String.class))).thenReturn(entityList);
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(mapper.patchPassword(any(PatchPasswordReq.class))).thenReturn(1);

        int result = service.patchPassword(patchPasswordReq);

        verify(mapper, times(1)).selPasswordBeforeLogin(any(String.class));
        verify(passwordEncoder, times(1)).encode(any(String.class));
        verify(mapper, times(1)).patchPassword(any(PatchPasswordReq.class));

        assertEquals(1, result);
    }
    @Test @DisplayName("로그인") // 로그인
    void signInPost() {
        HttpServletResponse res = null;
        SignInPostReq req1 = new SignInPostReq();
        req1.setUid("p1");
        req1.setUpw("1212");
        String password = passwordEncoder.encode(req1.getUpw());

        ParentsUser user1 = new ParentsUser();
        user1.setParentsId(1L);
        user1.setUid(req1.getUid());
        user1.setUpw(password);
        user1.setNm("길동");
        user1.setPhone("010-1234-1234");
        user1.setConnet("부");
        user1.setAuth("ROLE_USER");
        user1.setAcept(1);
        user1.setCreatedAt("2024-07-01 15:49:23");

        given(mapper.signInPost(req1.getUid())).willReturn(user1);

        String accessToken = "access-token";
        String refreshToken = "refresh-token";
        given(jwtTokenProvider.generateAccessToken(any(MyUser.class))).willReturn(accessToken);
        given(jwtTokenProvider.generateRefreshToken(any(MyUser.class))).willReturn(refreshToken);

        AppProperties.Jwt jwtProperties = mock(AppProperties.Jwt.class);
        given(appProperties.getJwt()).willReturn(jwtProperties);
        given(jwtProperties.getRefreshTokenCookieMaxAge()).willReturn(3600);

        try (MockedStatic<BCrypt> mockedStatic = mockStatic(BCrypt.class)) {
            mockedStatic.when(() -> BCrypt.checkpw(req1.getUpw(), password)).thenReturn(true);

            SignInPostRes res1 = service.signInPost(req1, res);

            assertEquals(user1.getParentsId(), res1.getParentsId(), "1. 이상");
            assertEquals(user1.getNm(), res1.getNm(),"2. 이상");
            assertEquals(accessToken, res1.getAccessToken(), "3. 이상");

            mockedStatic.verify(() -> BCrypt.checkpw(req1.getUpw(), password));
        }
        verify(mapper).signInPost(req1.getUid());
        verify(jwtTokenProvider).generateAccessToken(any(MyUser.class));
        verify(jwtTokenProvider).generateRefreshToken(any(MyUser.class));
        verify(cookieUtils).deleteCookie(res, "refresh-token");
        verify(cookieUtils).setCookie(res, "refresh-token",refreshToken,3600);
    }
    @Test @DisplayName("accessToken 가져오기 - 성공")
    void getAccessToken() {
        HttpServletRequest req = mock(HttpServletRequest.class);

        // 헤더 추가
        Enumeration<String> headers = Collections.enumeration(Arrays.asList("Header1", "Header2"));
        when(req.getHeaderNames()).thenReturn(headers);
        when(req.getHeader("Header1")).thenReturn("Value1");
        when(req.getHeader("Header2")).thenReturn("Value2");

        // 쿠키 추가
        Cookie cookie = new Cookie("refresh-token", "valid-refresh-token");
        when(req.getCookies()).thenReturn(new Cookie[]{cookie});
        given(cookieUtils.getCookie(req, "refresh-token")).willReturn(cookie);

        given(jwtTokenProvider.isValidateToken("valid-refresh-token")).willReturn(true);

        MyUser myUser = MyUser.builder()
                .userId(1)
                .role("ROLE_PARENTS")
                .build();
        MyUserDetails userDetails = new MyUserDetails();
        userDetails.setMyUser(myUser);

        given(jwtTokenProvider.getUserDetailsFromToken("valid-refresh-token")).willReturn(userDetails);
        given(jwtTokenProvider.generateAccessToken(myUser)).willReturn("new-access-token");

        Map<String, Object> result = service.getAccessToken(req);
        assertEquals("new-access-token", result.get("accessToken"));

        verify(cookieUtils).getCookie(req, "refresh-token");
        verify(jwtTokenProvider).isValidateToken("valid-refresh-token");
        verify(jwtTokenProvider).getUserDetailsFromToken("valid-refresh-token");
        verify(jwtTokenProvider).generateAccessToken(myUser);
    }
    @Test
    public void testGetFindPassword_ValidRequest_ShouldPutRandomCodeInMap() {
        GetFindPasswordReq req1 = new GetFindPasswordReq();
        req1.setUid(validGetFindPasswordReq.getUid());
        req1.setPhone("010-0000-0000");
        when(mapper.getParentUserList(any(GetFindPasswordReq.class))).thenReturn(Collections.singletonList(new ParentsUserEntity()));
        Map<String, String> map = new HashMap<>();

        service.getFindPassword(req1, map);

        assert map.containsKey("RANDOM_CODE");
        assert map.get("RANDOM_CODE").matches("\\d{6}");
    }
    @Test @DisplayName("비밀번호 찾기 문자발송 실패")
    void testGetFindPassword_smsSendError() {
        String code = "123456";
        List<ParentsUserEntity> list = List.of(new ParentsUserEntity());

        when(mapper.getParentUserList(getReq)).thenReturn(list);
        mockStatic(SmsSender.class);
        when(SmsSender.makeRandomCode()).thenReturn(code);
        doThrow(new RuntimeException("문자 메세지 보내기 실패")).when(smsService).sendPasswordSms(anyString(), any(), anyString());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.getFindPassword(getReq, map);
        });

        assertEquals("문자 메세지 보내기 실패", exception.getMessage());
    }
    @Test @DisplayName("전자서명")
    void signature() throws Exception {
        String path = "sign/1";
        String saveFileName = "randomFileName.png";
        String target = String.format("%s/%s", path, saveFileName);

        when(customFileUtils.makeFolders(path)).thenReturn(path);
        when(customFileUtils.makeRandomFileName(validFile)).thenReturn(saveFileName);
        doNothing().when(customFileUtils).transferTo(validFile, target);
        when(mapper.signature(signReq)).thenReturn(1);

        SignatureRes result = service.signature(validFile, signReq);

        assertNotNull(result);
        assertEquals(1, result.getSignId());
        assertEquals(saveFileName, result.getPics());

        verify(customFileUtils).makeFolders(path);
        verify(customFileUtils).makeRandomFileName(validFile);
        verify(customFileUtils).transferTo(validFile, target);
        verify(mapper).signature(signReq);
    }
    @Test @DisplayName("전자서명 실패 - 파일 없음")
    void signature_fileIsEmpty() {
        MultipartFile emptyFile = new MockMultipartFile("pic", "a.png", "image/png", new byte[]{});

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.signature(emptyFile, signReq);
        });

        assertEquals("서명 파일이 없습니다.", exception.getMessage());
    }
    @Test @DisplayName("전자서명 실패 - 파일 업로드 오류")
    void signature_fileUploadError() throws Exception {
        String path = "sign/1";
        String saveFileName = "randomFileName.png";
        String target = String.format("%s/%s", path, saveFileName);

        when(customFileUtils.makeFolders(path)).thenReturn(path);
        when(customFileUtils.makeRandomFileName(validFile)).thenReturn(saveFileName);
        doThrow(new RuntimeException("File transfer error")).when(customFileUtils).transferTo(validFile, target);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.signature(validFile, signReq);
        });

        assertTrue(exception.getMessage().contains("서명 등록 오류가 발생했습니다: File transfer error"));

        verify(customFileUtils).makeFolders(path);
        verify(customFileUtils).makeRandomFileName(validFile);
        verify(customFileUtils).transferTo(validFile, target);
    }
}



























