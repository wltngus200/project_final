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
public class HaesolOnlineMultiple {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long AnswerId;

    @Min(1) @Max(5)
    private Integer num;

    @ManyToOne
    @JoinColumn(name="que_id", nullable = false)
    private HaesolOnline haesolOnline;

    @Column(length=200, nullable = false)
    private String sentence;
}
