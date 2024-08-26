package com.green.fefu.socket.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetMemberChat {

    private ParentsDto parentsId;

    private Long roomId;

    private TeacherDto teaId;


    private List<ParentsDto> parents;

    @Builder
    public GetMemberChat(Long roomId, TeacherDto teaId, ParentsDto parentsId, List<ParentsDto> parents) {
        this.roomId = roomId;
        this.teaId = teaId;
        this.parentsId = parentsId;
        this.parents = parents;

    }


}
