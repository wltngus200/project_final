package com.green.fefu.entity;


import com.green.fefu.entity.dummy.Subject;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;


@Entity
@Getter
@Setter
//오답노트
public class StudentOnlineRecode extends CreatedAt{
    // 틀린 문제(정답), 해설, 학생 PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stuOnId;

    @ManyToOne
    @JoinColumn(name="stu_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name="subject_id", nullable = false)
    private Subject subject;

    // createdAt으로 시험 날짜

    @Column
    private String testTitle;



}
