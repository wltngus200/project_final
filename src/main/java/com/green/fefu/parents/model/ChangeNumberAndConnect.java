package com.green.fefu.parents.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ChangeNumberAndConnect {
    @Schema(description = "입력할 전화번호", required = true)
    private String phoneNumber ;
    @Schema(description = "자녀와의 관계", required = true)
    private String connect ;
    @Schema(description = "학부모 Pk", required = true)
    private String id;
}
