package com.green.fefu.parents.model;

import lombok.Data;

@Data
public class UpdateStudentParentsIdReq {
    private long studentPk;
    private long parentPk;
}
