package com.green.fefu.teacher.model.dataset;

public interface TeacherDBMaxLength {
    int TEACHER_ID_MAX_LENGTH = 20; //아이디
    int TEACHER_NAME_MAX_LENGTH = 20; //이름
    int TEACHER_PHONE_MAX_LENGTH = 20; //휴대폰 번호
    int TEACHER_EMAIL_MAX_LENGTH = 255; //이메일
    int TEACHER_GENDER_MAX_LENGTH = 5; //성별
    int TEACHER_AUTH_MAX_LENGTH = 30; //권한
    int TEACHER_ADDRESS_MAX_LENGTH = 300; //주소
    int TEACHER_PASSWORD_MAX_LENGTH = 70; // 비밀번호
}
