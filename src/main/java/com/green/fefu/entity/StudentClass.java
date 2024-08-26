package com.green.fefu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"stu_id", "class_id"}
                )
        }
)
@ToString
public class StudentClass  extends UpdatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer scId;

    @ManyToOne
    @JoinColumn(name = "stu_id" , nullable = false)
    private Student stuId;

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    private Class classId;


}
