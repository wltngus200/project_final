package com.green.fefu.admin.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.green.fefu.admin.model.dataset.ExceptionMsgDataSet.*;


@Getter
@Setter
@ToString
public class UpdateUserReq {
    @Min(value = 1, message = DIVISION_CODE_ERROR)
    @Max(value = 3, message = DIVISION_CODE_ERROR)
    @Schema(example = "1", description = "1-> 부모, 2-> 선생, 3-> 학생", required = true)
    private int p;
    @Positive(message = PK_DATA_ERROR)
    @Schema(example = "1", description = "선생/부모/학생 PK값", required = true)
    private Long pk;

    @Min(value = 0, message = STATE_DATA_ERROR)
    @Max(value = 3, message = STATE_DATA_ERROR)
    @Schema(example = "2", description = "선생/부모/학생 바꿀 스테이트 값")
    private int state;

    @Schema(example = "홍길동", description = "바꿀 학생 이름")
    private String userName;

    @Min(value = 0, message = STATE_DATA_ERROR)
    @Max(value = 6, message = STATE_DATA_ERROR)
    @Schema(example = "2", description = "바꿀 학생 학년")
    private int userGrade;

    @Min(value = 0, message = STATE_DATA_ERROR)
    @Max(value = 99, message = STATE_DATA_ERROR)
    @Schema(example = "2", description = "바꿀 학생 학급")
    private int userClass;
}
