package com.green.fefu.parentsBoard;

import com.green.fefu.parentsBoard.model.GetBoardReq;
import com.green.fefu.parentsBoard.model.GetBoardReqTea;
import com.green.fefu.parentsBoard.model.GetBoardRes;
import com.green.fefu.parentsBoard.model.PostBoardReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ParentsBoardMapper {
    int postBoard(PostBoardReq p);
    List<GetBoardRes> getBoard(GetBoardReqTea p);
    List<GetBoardRes> getBoardParent(GetBoardReq p);

}
