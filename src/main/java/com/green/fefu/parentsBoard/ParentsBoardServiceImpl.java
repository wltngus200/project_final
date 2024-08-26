package com.green.fefu.parentsBoard;

import com.green.fefu.common.AppProperties;
import com.green.fefu.common.model.ResultDto;
import com.green.fefu.notice.NoticeMapper;
import com.green.fefu.parentsBoard.model.GetBoardReq;
import com.green.fefu.parentsBoard.model.GetBoardReqTea;
import com.green.fefu.parentsBoard.model.GetBoardRes;
import com.green.fefu.parentsBoard.model.PostBoardReq;
import com.green.fefu.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParentsBoardServiceImpl {
    private final ParentsBoardMapper mapper;
    private final NoticeMapper noticeMapper;
    private final AuthenticationFacade authenticationFacade; //PK값을 제공(getLoginUserId();

    public int postBoard(PostBoardReq p){
        return mapper.postBoard(p);
    }

    /*선생님 조회*/
    public List<GetBoardRes> getBoard(GetBoardReqTea p){
        long teaId=authenticationFacade.getLoginUserId();
        p.setTeaId(teaId);
        int classId=noticeMapper.teacherHomeroom(teaId);
        p.setClassId(classId);
        return mapper.getBoard(p);
    }
    /*학부모 조회*/
    public List<GetBoardRes> getBoardParent(GetBoardReq p){
        long parentsId=authenticationFacade.getLoginUserId();
        p.setWriterId(parentsId);

        int classId=noticeMapper.childClassRoomList(p.getWriterId(),p.getStudentPk());
        p.setClassId(classId);
        return mapper.getBoardParent(p);
    }



}
