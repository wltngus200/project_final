package com.green.fefu.socket;



import com.green.fefu.entity.Parents;
import com.green.fefu.entity.Teacher;
import com.green.fefu.parents.ParentsUserServiceImpl;
import com.green.fefu.parents.repository.ParentRepository;
import com.green.fefu.security.AuthenticationFacade;
import com.green.fefu.socket.model.*;
import com.green.fefu.teacher.repository.TeacherRepository;
import com.green.fefu.teacher.service.TeacherServiceImpl;

import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.green.fefu.chcommon.ResponsDataSet.OK;

@CrossOrigin(origins = "*")
@Slf4j
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {
    private final TeacherServiceImpl teacherService;
    private final ChatService chatService;
    private final ParentsUserServiceImpl parentsUserService;
    private final ParentRepository parentRepository;
    private final TeacherRepository teacherRepository;
    private final AuthenticationFacade authenticationFacade;
    private final SimpMessagingTemplate template;

    @PostMapping(value = "parents/{teaId}/chat")
    @Operation(summary = "채팅방 만들기", description = "학부모 로그인 -> 선생님 Pk값 받고 같이 리턴 ")
    public String teacherCreateRoom(@PathVariable("teaId") Long teaId) {
        Parents parentsId = parentRepository.getReferenceById(authenticationFacade.getLoginUserId());
        parentsId.getName();

        Teacher teacher = teacherRepository.getReferenceById(teaId);
        Long roomId = chatService.createRoom(parentsId, teacher);
        log.info("Create Chat Room, senderNick: " + teacher.getName() + " 학부모님 입장했습니다");
        log.info("Create Chat Room, senderNick: " + parentsId.getName() + " 선생님이 입장했습니다");
        return "chatRoomId" + roomId.toString();
    }
    @PostMapping("/create")
    @Operation(summary = "채팅방 만들기", description = "선생님 로그인 -> 학부모 Pk값 받고 같이 리턴 ")
    public ResponseEntity<String> parentCreateRoom(
            @RequestBody List<Long> parentsId) {

        // 현재 로그인한 선생님의 ID를 가져옵니다.
        Teacher teaId = teacherRepository.getReferenceById(authenticationFacade.getLoginUserId());
        String teacherName = teaId.getName();

        // 최초의 부모 ID를 사용하여 채팅방을 생성합니다. (여기서는 첫 번째 부모 ID 사용)
        Parents firstParent = parentRepository.getReferenceById(parentsId.get(0)); // 첫 번째 부모 객체 가져오기
        Long roomId = chatService.createRoom(firstParent, teaId); // 첫 번째 학부모와 선생님으로 방 생성

        // 모든 학부모를 채팅방에 추가
        for (int i= 1 ; i < parentsId.size() ; i++) {
            Parents parent = parentRepository.getReferenceById(parentsId.get(i));
            chatService.addParentToRoom(roomId, parent); // 학부모를 채팅방에 추가하는 메서드 호출
            log.info("Create Chat Room, senderNick: {} 학부모님이 입장했습니다", parent.getName());
        }
        log.info("Create Chat Room, senderNick: {} 선생님이 입장했습니다", teacherName);

        // 생성된 채팅방 링크를 만듭니다.
        String roomLink = "chatRoomId" + roomId.toString();
        return ResponseEntity.ok(roomLink); // 생성된 링크 반환
    }

    @GetMapping(value = "chat/detail/{roomId}")
    @Operation(summary = "특정 채팅방 상세 정보 가져오기")
    @ResponseBody
     public List<ChatRoomDto> getChatRoomDetail(@PathVariable Long roomId) {
        log.info("GET Chat Room Detail, roomId: " + roomId);
        List<ChatRoomDto> a =   chatService.findRoom(roomId);
        System.out.println(a);;
       return a;
    }

    @GetMapping(value = "teacher/chats")
    @Operation(summary = "로그인한 선생님 전체 채팅 리스트 조회")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public List<GetMemberChat> findAllTeacher() {
        GetTeacherRoom teacherRoom = new GetTeacherRoom();
        teacherRoom.setTeaId(authenticationFacade.getLoginUserId());
        log.info("teaid: {}",teacherRoom.getTeaId() );
        List<GetMemberChat> dto = chatService.findAllTeacher(teacherRoom.getTeaId());
        log.info("dto: {}", dto);
//        for(int i = 0; i < dto.size(); i++){
////            ChatRoomDto dto1 = dto.get(i) ;
////            teacherRoom.setId(dto1.getRoomId()) ;
//            GetTeacherRoom res = new GetTeacherRoom();
//            res.setRoomId(dto.get(i).getRoomId());
//            res.setTeaId(dto.get(i).getTeaId().getTeaId());
//            result.add(res) ;
//        }
        return dto ;
    }

    @GetMapping(value = "parents/chats")
    @PreAuthorize("hasRole('PARENTS') or hasRole('ADMIN')")
    @Operation(summary = "로그인한 학부모 전체 채팅 리스트 조회")
    public List<GetMemberChat> findAllByMembersParent() {
        GetParentRoom getParentRoom = new GetParentRoom();
        getParentRoom.setParentId(authenticationFacade.getLoginUserId());
        List<GetMemberChat> dto = chatService.findAllByMembersParent(getParentRoom.getParentId());

//        for(int i = 0; i < dto.size(); i++){
////            ChatRoomDto dto1 = dto.get(i) ;
////            teacherRoom.setId(dto1.getRoomId()) ;
//            GetParentRoom res = new GetParentRoom();
//            res.setRoomId(dto.get(i).getRoomId());
//            res.setParentId(dto.get(i).getTeaId().getTeaId());
//            result.add(res) ;
//        }
        return dto;
    }

    @PostMapping(value = "chat/sender")
    @Operation(summary = "채팅 저장 ")
    public void message(@RequestBody ChatMsgDto message) {
        chatService.saveChat(message);
        template.convertAndSend("/sub/item/" + "/chat/" + message.getRoomId(), message);
    }
//    @MessageMapping("/sendMessage")
//    public void sendMessage(@Payload ChatMsgDto message) {
//        chatService.saveChat(message);
//        template.convertAndSend("/sub/item/chat/" + message.getRoomId(), message);
//    }
//
//    @MessageMapping("/joinRoom")
//    public void joinRoom(@Payload ChatMsgDto message) {
//        // 방에 참가 시 처리 로직
//        // 필요 시, 방 참가 이벤트를 클라이언트에 전송
//        template.convertAndSend("/sub/item/chat/" + message.getRoomId(),
//                new ChatMsgDto(message.getRoomId(), "System", message.getSender() + " joined the room"));
//    }
//
//    @MessageMapping("/leaveRoom")
//    public void leaveRoom(@Payload ChatMsgDto message) {
//        // 방을 떠날 때 처리 로직
//        // 필요 시, 방 퇴장 이벤트를 클라이언트에 전송
//        template.convertAndSend("/sub/item/chat/" + message.getRoomId(),
//                new ChatMsgDto(message.getRoomId(), "System", message.getSender() + " left the room"));
//    }

}



