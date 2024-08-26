package com.green.fefu.admin.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;

@Getter
@Setter
@ToString
public class FindUserListDto {
    private String state;

    private String id;
    private String name;
    private String uclass;
    private String grade;
    private Date date;
}
