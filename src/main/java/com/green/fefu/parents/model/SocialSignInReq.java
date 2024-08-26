package com.green.fefu.parents.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.fefu.security.SignInProviderType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SocialSignInReq {
    @Schema(description = "유저 아이디", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id ;

    @Schema(description = "연동 타입")
    private SignInProviderType providerType ;
}