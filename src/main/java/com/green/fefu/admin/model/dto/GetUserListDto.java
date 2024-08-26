package com.green.fefu.admin.model.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class GetUserListDto {
    private String pk;
    private String id;
    private String name;
    private String grade;
    private String createdAt;
}
