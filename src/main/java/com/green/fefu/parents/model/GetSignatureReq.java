package com.green.fefu.parents.model;

import lombok.Data;

@Data
public class GetSignatureReq {
    private long studentPk ;
    private int year ;
    private int semester ;
    private int examSign ;
}
