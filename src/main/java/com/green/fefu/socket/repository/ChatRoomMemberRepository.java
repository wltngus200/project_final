package com.green.fefu.socket.repository;

import com.green.fefu.entity.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
    // 기본 CRUD 작업은 JpaRepository에서 제공됩니다.
    // 필요한 경우 여기에 커스텀 쿼리 메서드를 추가할 수 있습니다.
}