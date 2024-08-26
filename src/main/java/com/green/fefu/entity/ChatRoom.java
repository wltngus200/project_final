package com.green.fefu.entity;

import com.green.fefu.entity.ChatMsg;
import com.green.fefu.entity.ChatRoomMember;
import com.green.fefu.entity.UpdatedAt;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "chat_room")
public class ChatRoom extends UpdatedAt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoomMember> members = new ArrayList<>();


    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMsg> chatMsgList = new ArrayList<>();

}