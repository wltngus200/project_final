package com.green.fefu.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "chat_room_member")
public class ChatRoomMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Parents parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    public ChatRoomMember() {
    }

    public ChatRoomMember(ChatRoom chatRoom, Parents parent) {
        this.chatRoom = chatRoom;
        this.parent = parent;
    }

    public ChatRoomMember(ChatRoom chatRoom, Teacher teacher) {
        this.chatRoom = chatRoom;
        this.teacher = teacher;
    }
}
