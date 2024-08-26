package com.green.fefu.teacher.model.dto;

import lombok.*;

@Builder
@Data
@ToString
public class EntityArgument {
    private Long pk;
    private String id;
    private String email;
    private String name;
    private String phone;
}
