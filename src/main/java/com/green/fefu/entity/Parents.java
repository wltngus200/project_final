package com.green.fefu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table
public class Parents extends User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long parentsId ;

    @Column(length = 20)
    private String subPhone ;

    @Column(length = 10)
    private String connect ;
}
