package com.green.fefu.parents.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.fefu.security.SignInProviderType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Setter;

@Data
public class SocialLoginReq {
    @JsonIgnore
    private long oath2Pk ;

    @Schema(description = "학부모 pk")
    private long parentId ;

    @Schema(description = "소셜 ID 값")
    private String socialId ;

    @Schema(description = "소셜에서 받아온 이름")
    private String socialName ;

    @Schema(description = "소셜에서 받은 Email")
    private String socialEmail ;

    @Schema(description = "소셜 타입")
    private SignInProviderType providerType ;
}
