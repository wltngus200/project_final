package com.green.fefu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
public class Student extends UserST {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stuId;

    @Column(nullable = false, unique = true)
    private Integer grade;

    @Column(length = 255)
    private String pic;

    @Column(length = 1000)
    private String etc;

    @Column(length = 30)
    private String engName;

    @Column(nullable = false, unique = true)
    private String randCode = UUID.randomUUID().toString().replace("-", "").substring(0, 7);

//    부모 pk 넣어야 함
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Parents parent;
}
