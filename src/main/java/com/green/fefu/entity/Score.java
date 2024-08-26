package com.green.fefu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Score extends UpdatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scoreId;

    @JoinColumn(name= "sc_id" , nullable = false)
    @ManyToOne
    private StudentClass scId;

    @Column
    private Integer semester;

    @Column
    private String year ;

    @Column
    private String name;

    @Column
    private Integer exam;

    @Column
    private Integer mark;

}
