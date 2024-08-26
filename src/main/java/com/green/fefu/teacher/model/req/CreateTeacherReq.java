package com.green.fefu.teacher.model.req;

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
public class CreateTeacherReq {

    //    필수
    @NotBlank(message = "아이디를 확인해주세요")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{6,12}$", message = ID_PATTERN_ERROR)
    @Schema(example = "test1234", description = "선생님 아이디", required = true)
    private String teacherId;

    @NotBlank(message = "비밀번호를 확인해주세요")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d|.*[!@#$%^&*()\\-_=+\\\\|\\[{\\]};:'\",<.>/?]).{8,20}$", message = PASSWORD_PATTERN_ERROR)
    @Schema(example = "Test1234!@#$", description = "선생님 비밀번호", required = true)
    private String password;

    @NotBlank(message = "이름을 확인해주세요")
    @Pattern(regexp = "^[가-힣a-zA-Z\\s-]+$", message = NAME_PATTERN_ERROR)
    @Schema(example = "홍길동", description = "선생님 이름", required = true)
    private String name;

    @NotBlank(message = "전화번호를 확인해주세요")
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = PHONE_PATTERN_ERROR)
    @Schema(example = "010-0000-0000", description = "선생님 전화번호", required = true)
    private String phone;

    @NotBlank(message = "이메일를 확인해주세요")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = EMAIL_PATTERN_ERROR)
    @Schema(example = "test1234@naver.com", description = "선생님 이메일", required = true)
    private String email;

    @NotBlank(message = "성별를 확인해주세요")
    @Schema(example = "남", description = "선생님 성별", required = true)
    private String gender;

    //    선택
    @Schema(example = "2024-06-28", description = "선생님 생년월일")
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$", message = BIRTH_TYPE_ERROR)
    private String birth;
    @Schema(example = "12345", description = "선생님 우편번호")
    private String zoneCode;
    @Schema(example = "서울 판교 1234", description = "선생님 주소")
    private String addr;
    @Schema(example = "101동", description = "선생님 상세 주소")
    private String detail;


    //    스웨거 출력 X
    @JsonIgnore
    private String teacherPk;
    @JsonIgnore
    private String fullAddr;
}
