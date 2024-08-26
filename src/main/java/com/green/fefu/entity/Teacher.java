package com.green.fefu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity
@Getter
@Setter
@ToString
public class Teacher extends UserST {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teaId;

}
