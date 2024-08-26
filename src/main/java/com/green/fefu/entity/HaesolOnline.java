package com.green.fefu.entity;


import com.green.fefu.entity.dummy.Subject;
import com.green.fefu.entity.dummy.TypeTag;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class HaesolOnline extends UpdatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long queId;

    @ManyToOne
    @JoinColumn(name="tea_id", nullable = false)
    private Teacher teaId;

    //============프론트 요구 정보(분류에 사용)=============

    // 학년정보 추가
    @Column(nullable = false)
    private Long classId;

    // 과목코드 1->국어, 2->수학
    @ManyToOne
    @JoinColumn(name="subjectCode", nullable = false)
    private Subject subjectCode;

    // 과목 세부 유형 ex. 1-> 문법, 2->어휘 ...
    @ManyToOne
    @JoinColumn(name="typeTag", nullable = false)
    private TypeTag typeTag;

    // 문제 유형 1->객관식 2->주관식
    @Min(1) @Max(2)
    private Integer queTag;

    //============실제 문제 출력될 요소=============

    // 난이도
    @Min(1) @Max(5)
    @Column(nullable = false)
    private Integer level;

    @Column(length=1000, nullable = false)
    private String question;

    @Column(length=1000, nullable = false)
    private String contents;

    @Column(nullable = false)
    private Integer answer;

    @Column(nullable = false)
    private String explanation; //문제 해설

    //사진 이름
    private String pic;


}
