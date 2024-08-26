package com.green.fefu.student.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AddChild {
    @NotBlank(message = "학생 코드를 입력해 주세요")
    @Schema(example = "ATT123", description = "학생 랜덤코드", required = true)
    String randCode;
}
