package com.green.fefu.admin.model.req;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.green.fefu.admin.model.dataset.ExceptionMsgDataSet.DIVISION_CODE_ERROR;

@Getter
@Setter
@ToString
public class FindUnAcceptListReq {
    @Min(value = 1, message = DIVISION_CODE_ERROR)
    @Max(value = 2, message = DIVISION_CODE_ERROR)
    private Integer p;

    private String searchWord;
}
