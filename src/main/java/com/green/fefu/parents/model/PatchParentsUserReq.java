package com.green.fefu.parents.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PatchParentsUserReq {
    @JsonIgnore
    @Schema(description = "학부모 PK")
    private long parentsId;
    @Schema(description = "학부모 이름")
    private String nm;
    @Schema(description = "관계")
    private String connet;
    @Schema(description = "학부모 전화번호")
    private String phone;
    @Schema(description = "추가 연락처")
    private String subPhone;
    @Schema(description = "학부모 email")
    private String email;
    @Schema(description = "주소")
    private String addr;
    @Schema(description = "상세 주소")
    private String detail;
    @Schema(description = "우편번호")
    private String zoneCode;

    public void setAddrs(String zoneCode, String addr, String detail) {
        if(zoneCode == null && addr == null){
            this.addr = null;
        } else {
            this.addr = zoneCode + "#" + addr + "#" + detail;
        }
    }
}
