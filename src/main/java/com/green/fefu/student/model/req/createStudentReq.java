package com.green.fefu.student.model.req;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.green.fefu.teacher.model.dataset.ExceptionMsgDataSet.*;

@Getter
@Setter
@ToString
public class createStudentReq {
    @NotBlank(message = "학년 학급 번호를 확인해주세요")
    @Pattern(regexp = "^[1-6](0[1-9]|[1-9][0-9])(0[1-9]|[1-9][0-9])$", message = STUDENT_GRADE_DATA_ERROR)
    @Schema(example = "10101", description = "학생 학년 반 번호 ( 1학년 1반 1번 )", required = true)
    private String grade;
    @NotBlank(message = "이름을 확인해주세요")
    @Pattern(regexp = "^[가-힣a-zA-Z\\s-]+$", message = NAME_PATTERN_ERROR)
    @Schema(example = "홍길동", description = "학생 이름", required = true)
    private String name;

    @NotBlank(message = "성별를 확인해주세요")
    @Schema(example = "남", description = "학생 성별", required = true)
    private String gender;

    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$", message = BIRTH_TYPE_ERROR)
    @Schema(example = "1998-01-01", description = "학생 생년월일", required = true)
    private String birth;


    @Schema(example = "123 # 서울 판교로 112", description = "학생 주소", required = true)
    private String addr;

    @Schema(example = "갑각류 알러지 있음", description = "학생 추가정보 기입", required = true)
    private String etc;

    @Schema(example = "adela", description = "학생 영어이름", required = true)
    private String engName;

    @NotBlank(message = "전화번호를 확인해주세요")
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = PHONE_PATTERN_ERROR)
    @Schema(example = "010-0000-0000", description = "학생 전화번호", required = true)
    private String phone;

//    @Schema(example = "1", description = "선생 pk (반배정 안된 선생만 넣기!!!!)")
//    private long teacherPk;

    @NotBlank(message = "아이디를 확인해주세요")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{6,12}$", message = ID_PATTERN_ERROR)
    @Schema(example = "student1", description = "학생 아이디")
    private String studentId;

    @NotBlank(message = "비밀번호를 확인해주세요")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d|.*[!@#$%^&*()\\-_=+\\\\|\\[{\\]};:'\",<.>/?]).{8,20}$", message = PASSWORD_PATTERN_ERROR)
    @Schema(example = "Stu1234!@#$", description = "학생 비밀번호")
    private String studentPw;

    @NotBlank(message = "이메일를 확인해주세요")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = EMAIL_PATTERN_ERROR)
    @Schema(example = "test1234@naver.com", description = "선생님 이메일", required = true)
    private String email;

    @JsonIgnore
    private long pk;
    @JsonIgnore
    private String pic;
}
