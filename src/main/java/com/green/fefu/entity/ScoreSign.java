package com.green.fefu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class ScoreSign extends CreatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long signId;

    @JoinColumn(name = "student_pk")
    @ManyToOne
    private Student studentPk;

    @Column
    private int year;

    @Column
    private int semester;

    @Column
    private String pic;

    @Column
    private int examSign;


}
