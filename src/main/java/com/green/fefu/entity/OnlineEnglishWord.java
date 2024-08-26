package com.green.fefu.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class OnlineEnglishWord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wordPk;

    @Column(length=1000)
    private String word;

    @Column(length=1000)
    private String answer;

    private String pic;

    @ManyToOne
    @JoinColumn(name="tea_id", nullable = false)
    private Teacher teaId;

    @Min(1) @Max(6)
    private Long grade;

    // 난이도
    // private Integer level;


    // 학년정보 추가
    // private Long classId;

}
