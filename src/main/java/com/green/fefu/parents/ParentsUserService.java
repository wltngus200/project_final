package com.green.fefu.parents;

import com.green.fefu.parents.model.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ParentsUserService {
    // 회원가입
    int postParentsUser(PostParentsUserReq p);
    // 아이디, 이메일 중복확인
    String checkEmailOrUid(CheckEmailOrUidReq c);
    // 회원정보 조회
    ParentsUserEntity getParentsUser(String id);
    // 회원정보 수정
    int patchParentsUser(PatchParentsUserReq p);
    // 아이디 찾기
    GetFindIdRes getFindId(GetFindIdReq req);
    // 비밀번호 수정
    int patchPassword(PatchPasswordReq req);
    // 로그인
    SignInPostRes signInPost(SignInPostReq p, HttpServletResponse res);
    // 토큰정보 확인
    Map getAccessToken(HttpServletRequest req);
    // 비밀번호 찾기
    void getFindPassword(GetFindPasswordReq req, Map map);
    // 전자서명 관련
    SignatureRes signature(MultipartFile pic, SignatureReq req) ;
    // 학생정보 조회
    List<GetStudentParentsRes> getStudentParents(String token) ;
}
