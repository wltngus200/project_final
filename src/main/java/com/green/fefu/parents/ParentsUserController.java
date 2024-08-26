package com.green.fefu.parents;

import com.green.fefu.parents.model.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ParentsUserController {
    // 회원가입
    ResponseEntity<Integer> postParents(PostParentsUserReq p) ;
    // 아이디, 이메일 중복확인
    ResponseEntity<String> checkEmailOrUid(CheckEmailOrUidReq req) ;
    // 회원정보 조회
    ResponseEntity<ParentsUserEntity> getParentsUser(HttpServletRequest p) ;
    // 회원정보 수정
    ResponseEntity<Integer> patchParentsUser(PatchParentsUserReq p) ;
    // 아이디 찾기
    ResponseEntity<GetFindIdRes> getFindId(GetFindIdReq p) ;
    // 비밀번호 수정
    ResponseEntity<Integer> patchPassword(PatchPasswordReq p) ;
    // 로그인
    ResponseEntity<SignInPostRes> signInPost(SignInPostReq p, HttpServletResponse res) ;
    // 토큰정보 확인
    ResponseEntity<Map> getAccessToken(HttpServletRequest req) ;
    // 비밀번호 찾기
    ResponseEntity<GetFindPasswordRes> getFindPassword(GetFindPasswordReq req) ;
    // 전자서명 ( MultipartFile 이용 )
    ResponseEntity<SignatureRes> signature(MultipartFile pic, SignatureReq p) ;
    // 학생정보 조회
    ResponseEntity<List<GetStudentParentsRes>> getStudentParents(HttpServletRequest req) ;
}
