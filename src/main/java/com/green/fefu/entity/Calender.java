package com.green.fefu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Calender {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long calenderId;

//    행사 타입
    @Column(nullable = false)
    private Integer type;
//    행사 일정
@Column(nullable = false)
    private Date schedule;
//    행사 이름
@Column(nullable = false)
    private String name;
}