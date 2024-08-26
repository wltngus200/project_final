package com.green.fefu.socket.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.green.fefu.entity.ChatMsg;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatMsgDto {
    private String msg;

    private Long roomId;

    private String sender;


    private String sendTime;  // LocalDateTime 대신 String으로 변경

    private Long teaId;
    private Long parentId;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 생성자
    public ChatMsgDto(String msg, Long roomId, String sender, LocalDateTime sendTime) {
        this.msg = msg;
        this.roomId = roomId;
        this.sender = sender;
        this.sendTime = sendTime.format(formatter);  // LocalDateTime을 형식화된 String으로 변환
    }

    public ChatMsgDto(Long roomId, String system, String s) {
    }
}
