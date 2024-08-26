package com.green.fefu.teacher.test;

import com.green.fefu.teacher.model.req.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;

public interface TeacherController {
//    선생 회원가입
    ResponseEntity CreateTeacher(CreateTeacherReq p);
//    선생 로그인
    ResponseEntity LogInTeacher(LogInTeacherReq p, HttpServletResponse res);
//    중복 확인
    ResponseEntity CheckDuplicate(CheckDuplicateReq p);
//    선생 아이디 찾기
    ResponseEntity FindTeacherId(FindTeacherIdReq p);
//    선생 비밀번호 찾기
    ResponseEntity FindTeacherPassword(FindTeacherPasswordReq p);
//    선생 비밀번호 변경
    ResponseEntity ChangePassWord(ChangePassWordReq p);
//    선생 마이페이지
    ResponseEntity TeacherDetail();
//    선생 정보 변경
    ResponseEntity ChangeTeacher(ChangeTeacherReq p);
}
