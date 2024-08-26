package com.green.fefu.common.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInRes {
    @Schema(example = "1", description = "유저PK")
    private long userId;
    @Schema(example = "홍길동", description = "유저이름")
    private String pic;
    @Schema(example = "ee3874e3-c34a-4ab6-9f3a-12ee11c0042f.jpg", description = "유저 프로필 이미지 ")
    private String nm;

    private String accessToken;

//    private String refreshToken;
}
