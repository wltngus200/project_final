package com.green.fefu.student.model.req;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.green.fefu.student.model.dataset.ExceptionMsgDataSet.*;
import static com.green.fefu.teacher.model.dataset.ExceptionMsgDataSet.STUDENT_GRADE_DATA_ERROR;

@Getter
@Setter
@ToString
public class studentAdvanceGradeReq {
    @NotBlank(message = REQUIRED_DATA_ERROR)
    @Schema(example = "1", description = "학생 pk", required = true)
    private String studentPk;
    @NotBlank(message = REQUIRED_DATA_ERROR)
    @Pattern(regexp = "^[1-6](0[1-9]|[1-9][0-9])(0[1-9]|[1-9][0-9])$", message = STUDENT_GRADE_DATA_ERROR)
    @Schema(example = "10101", description = "진학 하는 학년/반/번호", required = true)
    private String grade;

    @JsonIgnore
    private String subNumber;
    @JsonIgnore
    private String etc;
}
