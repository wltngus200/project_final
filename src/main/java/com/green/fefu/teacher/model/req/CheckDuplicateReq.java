package com.green.fefu.teacher.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.green.fefu.teacher.model.dataset.ExceptionMsgDataSet.EMAIL_PATTERN_ERROR;
import static com.green.fefu.teacher.model.dataset.ExceptionMsgDataSet.ID_PATTERN_ERROR;

@Getter
@Setter
@ToString
public class CheckDuplicateReq {
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{6,12}$", message = ID_PATTERN_ERROR)
    @Schema(example = "test1234", description = "선생님 아이디")
    private String id;

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = EMAIL_PATTERN_ERROR)
    @Schema(example = "test1234@naver.com", description = "선생님 이메일")
    private String email;
}
