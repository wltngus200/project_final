package com.green.fefu.entity;

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
public class OnlineEnglishListening {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long listeningPk;

    private String question;

    private String answer;

    private String pic;

    private String sentence;

    @Min(1) @Max(6)
    private Long grade;

    @ManyToOne
    @JoinColumn(name="tea_id", nullable = false)
    private Teacher teaId;
}
