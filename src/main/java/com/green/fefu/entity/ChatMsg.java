package com.green.fefu.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;


@Entity
@Getter
@Setter
@Table(name = "chat_msg")
public class ChatMsg extends UpdatedAt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

    private String sender;

    private String message;

    @ManyToOne
    @JoinColumn(name = "tea_id")

    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "parents_id")
    private Parents parents;

}
