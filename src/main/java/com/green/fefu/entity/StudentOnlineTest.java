package com.green.fefu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class StudentOnlineTest {
// 시험 내용= 한 시험에 20개씩 반복되는 거
    @Id // 임의값
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stuTesPK;

    @ManyToOne
    @JoinColumn(name="stuon_id")
    //온라인 시험의 PK값(학생 pk, 과목, 제목 저장)
    private StudentOnlineRecode studentOnlineRecode;

    @ManyToOne
    @JoinColumn(name="que_id")
    private HaesolOnline haesolOnline;

    @Column
    private Integer stuAnswer; //문제마다 학생의 답안
}
