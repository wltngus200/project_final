package com.green.fefu.entity.dummy;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Entity
public class TypeTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long TagId; //pk

    @ManyToOne
    @JoinColumn(name="subject_id", nullable = false)
    private Subject subject; // 과목 1->국어 2->수학

    @Column(unique = true)
    private Integer typeNum; // 11->문법 12-> 작문 ...

    private String tagName;

}
