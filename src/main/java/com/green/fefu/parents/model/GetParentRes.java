package com.green.fefu.parents.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@ToString
public class GetParentRes {

    private long parentsId;
    private String name;
    private String studentName;
    private String connect;
}
