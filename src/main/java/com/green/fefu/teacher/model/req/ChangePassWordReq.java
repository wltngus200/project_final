package com.green.fefu.teacher.model.req;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.green.fefu.teacher.model.dataset.ExceptionMsgDataSet.ID_PATTERN_ERROR;
import static com.green.fefu.teacher.model.dataset.ExceptionMsgDataSet.PASSWORD_PATTERN_ERROR;

@Getter
@Setter
@ToString
public class ChangePassWordReq {
    @NotBlank(message = "아이디를 확인해주세요")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{6,12}$", message = ID_PATTERN_ERROR)
    @Schema(example = "test1234", description = "선생님 아이디")
    private String teacherUid;

    @NotBlank(message = "비밀번호를 확인해주세요")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d|.*[!@#$%^&*()\\-_=+\\\\|\\[{\\]};:'\",<.>/?]).{8,20}$", message = PASSWORD_PATTERN_ERROR)
    @Schema(example = "Test1234!@#$", description = "선생님 비밀번호", required = true)
    private String passWord;

    @NotBlank(message = "비밀번호를 확인해주세요")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d|.*[!@#$%^&*()\\-_=+\\\\|\\[{\\]};:'\",<.>/?]).{8,20}$", message = PASSWORD_PATTERN_ERROR)
    @Schema(example = "Test1234!@#$", description = "선생님 비밀번호", required = true)
    private String oldPassWord ;

    @JsonIgnore
    private long pk;
}
