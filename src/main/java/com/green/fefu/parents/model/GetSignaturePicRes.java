package com.green.fefu.parents.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class GetSignaturePicRes {
    private long signId ;
    private long stuId ;
    private int examSign ;
    private int semester ;
    private int year ;
    private String pic ;
}
