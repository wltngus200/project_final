package com.green.fefu.teacher.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.green.fefu.teacher.model.dataset.ExceptionMsgDataSet.ID_PATTERN_ERROR;
import static com.green.fefu.teacher.model.dataset.ExceptionMsgDataSet.PHONE_PATTERN_ERROR;

@Getter
@Setter
@ToString
public class FindTeacherPasswordReq {
    @NotBlank(message = "아이디를 확인해주세요")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{6,12}$", message = ID_PATTERN_ERROR)
    @Schema(example = "test1234", description = "선생님 아이디", required = true)
    private String id;

    @NotBlank(message = "전화번호를 확인해주세요")
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = PHONE_PATTERN_ERROR)
    @Schema(example = "010-0000-0000", description = "선생님 전화번호", required = true)
    private String phone;
}
