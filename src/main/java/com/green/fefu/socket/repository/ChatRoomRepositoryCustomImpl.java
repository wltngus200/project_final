package com.green.fefu.socket.repository;

import com.green.fefu.entity.ChatRoom;
import com.green.fefu.entity.Teacher;
import com.green.fefu.entity.Parents;
import com.green.fefu.socket.model.ChatRoomDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepositoryCustomImpl extends JpaRepository<ChatRoom, Long> {

    /**
     * 채팅방 ID로 채팅방 DTO를 조회합니다.
     */
//    @Query("SELECT new com.green.fefu.socket.model.ChatRoomDto(c.id, " +
//            "cm.teacher, cm.parent) " +
//            "FROM ChatRoom c LEFT JOIN c.members cm WHERE c.id = :id")
    Optional<ChatRoom> findById(Long roomId);

    /**
     * 부모와 선생님으로 채팅방을 조회합니다.
     */
    @Query("SELECT c FROM ChatRoom c " +
            "JOIN c.members cm1 JOIN c.members cm2 " +
            "WHERE cm1.parent = :parents AND cm2.teacher = :teacher")
    Optional<ChatRoom> findByMembersParentAndMembersTeacher(@Param("parents") Parents parents, @Param("teacher") Teacher teacher);

    /**
     * 선생님이 속한 모든 채팅방을 조회합니다.
     */
    @Query("SELECT DISTINCT c FROM ChatRoom c JOIN ChatRoomMember  cm  ON c.id = cm.chatRoom.id WHERE cm.teacher = :tea_id")
    List<ChatRoom> findAllByMembersTeacher(@Param("tea_id") Teacher teacher);

    /**
     * 부모가 속한 모든 채팅방을 조회합니다.
     */
    @Query("SELECT DISTINCT c FROM ChatRoom c JOIN ChatRoomMember cm WHERE cm.parent = :parent")
    List<ChatRoom> findAllByMembersParent(@Param("parent") Parents parent);


}