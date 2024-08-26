package com.green.fefu.score.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class DtoDetail<T>{
    private List<T> list = new ArrayList<T>();

    private  T studentPk;

    private T classRank;

    private T signResult;

}
