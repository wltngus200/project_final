package com.green.fefu.parentsBoard;

import ch.qos.logback.core.rolling.helper.IntegerTokenConverter;
import com.green.fefu.common.model.ResultDto;
import com.green.fefu.parentsBoard.model.GetBoardReq;
import com.green.fefu.parentsBoard.model.GetBoardReqTea;
import com.green.fefu.parentsBoard.model.GetBoardRes;
import com.green.fefu.parentsBoard.model.PostBoardReq;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/p_board")
@RequiredArgsConstructor
public class ParentsBoardControllerImpl {
    private final ParentsBoardServiceImpl service;
    @PostMapping
    @Operation(summary = "학부모 게시판 작성 - 제작중")
    @PreAuthorize("hasRole('PARENTS') or hasRole('TEACHER')")
    public ResultDto<Integer> postBoard(PostBoardReq p){
        int result=service.postBoard(p);
        return ResultDto.<Integer>builder()
                .statusCode(HttpStatus.OK)
                .resultMsg(HttpStatus.OK.toString())
                .result(result)
                .build();
    }
    /*선생님이 확인*/
    @GetMapping
    @Operation(summary = "학부모 게시판 조회(선생님) - 제작중")
    @PreAuthorize("hasRole('TEACHER')")
    public ResultDto<List<GetBoardRes>> getBoard(GetBoardReqTea p){
        List<GetBoardRes> result=service.getBoard(p);
        return ResultDto.<List<GetBoardRes>>builder()
                .statusCode(HttpStatus.OK)
                .resultMsg(HttpStatus.OK.toString())
                .result(result)
                .build();
    }

    /*자신이 작성한 것 확인*/
    @GetMapping("tmtmp")
    @Operation(summary = "학부모 게시판 조회(학부모) - 제작중")
    @PreAuthorize("hasRole('PARENTS')")
    public ResultDto<List<GetBoardRes>> getBoardParent(GetBoardReq p){
        List<GetBoardRes> result=service.getBoardParent(p);
        return ResultDto.<List<GetBoardRes>>builder()
                .statusCode(HttpStatus.OK)
                .resultMsg(HttpStatus.OK.toString())
                .result(result)
                .build();
    }
    //알림기능 실현
    //컬럼 : content , classId , writerType , writerId(PK),  전송 날짜 ,  읽음 여부(1, 2)
    //학부모 10개만 표시 학부모가 전송시 선생님에게 알림 학부모가 교직원이 읽었는지 체크 가능
    //선생님의 읽음 처리 되는 시점-> 느낌표 눌렀을 때 페이지 이동 -> 버튼 하나 눌렀을때 완료

}
