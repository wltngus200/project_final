package com.green.fefu.socket.repository;

import com.green.fefu.entity.ChatMsg;
import com.green.fefu.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMsgRepository extends JpaRepository<ChatMsg, Long> {
    @Query("SELECT cm FROM ChatMsg cm WHERE cm.chatRoom = :chatRoom ")
    List<ChatMsg> findByChatRoomOrderBySendTimeAsc(@Param("chatRoom") ChatRoom chatRoom);
}