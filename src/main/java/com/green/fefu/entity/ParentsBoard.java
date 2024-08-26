package com.green.fefu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ParentsBoard extends UpdatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Column(length=100)
    private String title;

    @Column(length=1000)
    private String content;

    @ManyToOne
    @JoinColumn(name = "parents_id", nullable = false)
    private Parents parentsId;

    //학부모도 없음!
    //@ManyToOne
    //@JoinColumn(name="parents_id", nullable=false)
    //private Parents parents;
}
