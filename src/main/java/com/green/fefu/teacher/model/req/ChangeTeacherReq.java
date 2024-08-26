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
public class ChangeTeacherReq {
    @NotBlank(message = "이름을 확인해주세요")
    @Pattern(regexp = "^[가-힣a-zA-Z\\s-]+$", message = NAME_PATTERN_ERROR)
    @Schema(example = "홍길동", description = "선생님 이름")
    private String name;

    @NotBlank(message = "전화번호를 확인해주세요")
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = PHONE_PATTERN_ERROR)
    @Schema(example = "010-0000-0000", description = "선생님 전화번호")
    private String phone;

    @NotBlank(message = "이메일를 확인해주세요")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = EMAIL_PATTERN_ERROR)
    @Schema(example = "test1234@naver.com", description = "선생님 이메일")
    private String email;

    @Schema(example = "12345", description = "선생님 우편번호")
    private String zoneCode;

    @Schema(example = "서울 판교 1234", description = "선생님 주소")
    private String addr;

    @Schema(example = "101동", description = "선생님 상세주소")
    private String detail;

    @JsonIgnore
    private long pk;
    @JsonIgnore
    private String fullAddr;
}
