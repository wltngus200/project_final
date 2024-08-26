package com.green.fefu.calender.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class CalenderReq {
    @Schema(example = "2024-08-07")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "YYYY-MM-DD 형식을 맞춰주세요")
    private String calenderDate;
    @Schema(example = "수요일은 다 먹는 날")
    @NotBlank(message = "일정 명을 적어주세요")
    private String calenderName;

    @Schema(example = "1->수다날 2->시험일 3->휴무일")
    @Min(value = 1, message = "일정 타입은 1보다 작을 수 없습니다.")
    @Max(value = 3, message = "일정 타입은 3보다 클 수 없습니다.")
    private Integer calenderType;
}
