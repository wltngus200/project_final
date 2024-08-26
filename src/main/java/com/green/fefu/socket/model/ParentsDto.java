package com.green.fefu.socket.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.green.fefu.entity.Parents;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParentsDto {
    private Long parentId;

    private String name;


    public ParentsDto(Parents data) {
        this.parentId = data.getParentsId();
        this.name = data.getName();
    }
}
