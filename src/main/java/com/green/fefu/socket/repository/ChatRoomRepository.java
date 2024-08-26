package com.green.fefu.socket.repository;

import com.green.fefu.entity.ChatRoom;
import com.green.fefu.entity.Parents;
import com.green.fefu.entity.Teacher;
import com.green.fefu.socket.model.ChatRoomDto;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;


public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findByMembersParentAndMembersTeacher(Parents parent, Teacher teacher);

    List<ChatRoom> findAllByMembersParent(Parents parent);

    List<ChatRoom> findAllByMembersTeacher(Teacher teacher);

    ChatRoom findById(ChatRoom id);
}