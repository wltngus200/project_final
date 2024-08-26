package com.green.fefu.socket.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetMemberParentsId {


        private ParentsDto parentsId;

        private Long roomId;



        @Builder
        public GetMemberParentsId(Long roomId, TeacherDto teaId, ParentsDto parentsId) {
            this.roomId = roomId;

            this.parentsId = parentsId;

        }
}
