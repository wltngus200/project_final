package com.green.fefu.student.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class deleteStudentReq {
    @NotBlank(message = "pk값은 필수입니다.")
    @Schema(example = "1",description = "삭제할 유저 pk값")
    private Long pk;
    @NotBlank(message = "변경할 값은 필수입니다.")
    @Schema(example = "2",description = "변경할 값 ( 1 -> 재학중, 2 -> 졸업, 3 -> 전학 )")
    private Integer state;
}
