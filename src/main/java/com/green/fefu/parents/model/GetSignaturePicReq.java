package com.green.fefu.parents.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class GetSignaturePicReq {
    @Schema(description = "사인 Pk", required = true)
    private long signPk ;
}
