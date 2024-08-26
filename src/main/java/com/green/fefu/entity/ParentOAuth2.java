package com.green.fefu.entity;

import com.green.fefu.security.SignInProviderType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@ToString
@Entity
@Table (
        name = "parent_oath2" ,
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"provider_type", "parent_id"}
                )
        }
)
public class ParentOAuth2 extends UpdatedAt {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long oath2Id ;
    private String id ;
    private String name ;
    private String email ;
    @Column(nullable = false) @ColumnDefault("4")
    private SignInProviderType providerType ;
    @ManyToOne @JoinColumn(name = "parent_id", nullable = false)
    private Parents parentsId ;
}
