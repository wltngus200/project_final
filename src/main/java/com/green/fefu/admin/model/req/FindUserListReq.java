package com.green.fefu.admin.model.req;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.green.fefu.admin.model.dataset.ExceptionMsgDataSet.*;

@Getter
@Setter
@ToString
public class FindUserListReq {
//    @NotBlank(message = DIVISION_CODE_ERROR)
    @Min(value = 1, message = DIVISION_CODE_ERROR)
    @Max(value = 2, message = DIVISION_CODE_ERROR)
    private Integer p;
//    @NotBlank(message = DIVISION_ERROR)
    @Min(value = 1, message = DIVISION_ERROR)
    @Max(value = 2, message = DIVISION_ERROR)
    private Integer check;

    private String searchWord;
}
