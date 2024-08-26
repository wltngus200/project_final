package com.green.fefu.parents.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ParentsUserEntity {
    private long parentsId;

    @Schema(description = "아이디")
    private String uid;
    @Schema(description = "비밀번호")
    private String upw;
    @Schema(description = "이름")
    private String nm;

    @Schema(description = "전화번호")
    private String phone;

    @Schema(description = "비상연락망")
    private String subPhone;

    @Schema(description = "이메일")
    private String email;

    @Schema(description = "관계")
    private String connet;

    @Schema(description = "권한 ROLE_USER")
    private String auth;

    @Schema(description = "우편번호")
    private String zoneCode ;

    @Schema(description = "주소")
    private String addr;

    @Schema(description = "가입 승인 여부")
    private int acept;
}
