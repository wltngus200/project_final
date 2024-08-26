package com.green.fefu.semester.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@EqualsAndHashCode
@ToString
public class SemesterReq {
    @JsonIgnore
    private  long semesterId;

    private  Integer option;

}
