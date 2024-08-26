package com.green.fefu.socket.model;

import com.green.fefu.entity.Parents;
import com.green.fefu.entity.Teacher;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class GetTeacherRoom {

    private Long teaId;

    private Long roomId;

    private Long classId;

    private Long gradleId;

}
