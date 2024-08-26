package com.green.fefu.student.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StudentSignInReq {
    @Schema(example = "ghdrlfehd111")
    private String studentUid;
    @Schema(example = "Test1234!@#$")
    private String studentPwd;
}
