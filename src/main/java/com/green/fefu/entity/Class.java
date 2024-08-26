package com.green.fefu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
public class Class extends UpdatedAt {
    @Id
    private Integer classId ;

    @OneToOne
    @JoinColumn(name = "tea_id",nullable = false)
    private Teacher teaId ;
}
