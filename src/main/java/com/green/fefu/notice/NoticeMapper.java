package com.green.fefu.notice;

import com.green.fefu.notice.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoticeMapper {
    int postNotice(PostNoticeReq p);
    List<GetNoticeRes> getNotice(GetNoticeReq p);

    //반에서 최신 알림장 1개 조회
    List<GetNoticeRes> getNoticeLatest(GetNoticeReq p);

    int putNotice(PutNoticeReq p); //구현 예정
    int deleteNotice(DeleteNoticeReq p);



    //학부모와 교사의 관계된 학반 조회
    Integer teacherHomeroom(long teaId);

    Integer childClassRoomList(long parentsId, long studentPk);

    Integer studentClass(long stuId);


    //TDD를 위한 메소드
    List<GetNoticeRes> getNoticeForTDD();

    List<GetNoticeRes> getNoticeForTDDJustOne(GetOneNoticeForTDD p);
}
