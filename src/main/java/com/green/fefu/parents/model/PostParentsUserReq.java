package com.green.fefu.parents.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.fefu.security.SignInProviderType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class PostParentsUserReq {
    @JsonIgnore
    private long parentsId;
    @Schema(description = "아이디", required = true)
    private String uid;

    @Schema(description = "비밀번호", required = true)
    private String upw;

    @Schema(description = "이름", required = true)
    private String nm;

    @Schema(description = "전화번호", required = true)
    private String phone;

    @Schema(description = "비상연락망")
    private String subPhone;

    @Schema(description = "이메일")
    private String email;

    @Schema(description = "관계", required = true)
    private String connect;

    @Schema(description = "우편번호")
    private String zoneCode ;

    @Schema(description = "주소")
    private String addr;

    @Schema(description = "상세 주소")
    private String detail;

    @Schema(description = "학생 고유 랜덤코드값.", required = true)
    private String studentRandomCode;

    @JsonIgnore
    private String addrs ;

    public void setAddrs(String zoneCode, String addr, String detail) {
        if(zoneCode == null && addr == null){
            this.addrs = null;
        } else {
            this.addrs = zoneCode + "#" + addr + "#" + detail;
        }
    }
}
