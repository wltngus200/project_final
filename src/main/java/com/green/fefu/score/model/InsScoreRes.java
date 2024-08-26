package com.green.fefu.score.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class InsScoreRes {
   private int studentPk;

   private String year;

   private int semester;

   private int gradle;
}
