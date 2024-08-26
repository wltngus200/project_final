package com.green.fefu.parents.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class ParentsUser {
    @JsonIgnore
    private long parentsId;

    private String uid;

    private String upw;

    private String nm;

    private String phone;

    private String subPhone;

    private String email;

    private String connect;

    private String auth;

    private String addr;

    private int accept;

    private String createdAt;

    private String updatedAt;
}
