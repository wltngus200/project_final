package com.green.fefu.notice;

import com.green.fefu.common.model.ResultDto;
import com.green.fefu.notice.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RequestMapping("api/notice")
@Tag(name="알림장 Notice",description="알림장 CRUD")
public class NoticeControllerImpl implements NoticeController{
    private final NoticeService service;


    @PostMapping("")
    @Operation(summary = "알림장 작성",
            description =
                    "<strong> 변수명 : title </strong> <p> 알림장 제목 ex)금요일 생일파티 공지 </p>" +
                    "<strong> 변수명 : content </strong> <p> 알림장 내용 ex)과자 1봉 가져오기 </p>" +
                    "<strong> 변수명 : STATE </strong> <p> 알림장 항목(1 : 알림장 / 2 : 준비물)  ex)1 </p>")
    @PreAuthorize("hasRole('TEACHER')")
    public ResultDto<Integer> postNotice(@RequestBody PostNoticeReq p){
        int result = service.postNotice(p);
        return ResultDto.<Integer>builder()
                .statusCode(HttpStatus.OK)
                .resultMsg("성공적으로 업로드 되었습니다.")
                .result(result)
                .build();
    }

    @GetMapping("")
    @Operation(summary = "알림장 조회",
            description = "<strong> 변수명 : STATE </strong> <p> 알림장 항목(1 : 알림장 / 2 : 준비물)  ex)1 </p>")
    @PreAuthorize("hasRole('PARENTS') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResultDto<Map<String, List<GetNoticeRes>>> getNotice(@ModelAttribute @ParameterObject GetNoticeReq p){
        Map<String, List<GetNoticeRes>> list=service.getNotice(p);
        return ResultDto.<Map<String, List<GetNoticeRes>>>builder()
                .statusCode(HttpStatus.OK)
                .resultMsg("성공적으로 조회되었습니다.")
                .result(list)
                .build();
    }
    @GetMapping("/main")
    @Operation(summary = "최신 알림장 조회",
            description = "<strong> 변수명 : STATE </strong> <p> 알림장 항목(1 : 알림장 / 2 : 준비물)  ex)1 </p>")
    @PreAuthorize("hasRole('PARENTS') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResultDto<Map<String, GetNoticeRes>> getNoticeLatest(@ModelAttribute @ParameterObject GetNoticeReq p){
        Map<String, GetNoticeRes> twoThing =service.getNoticeLatest(p);
        return ResultDto.<Map<String, GetNoticeRes>>builder()
                .statusCode(HttpStatus.OK)
                .resultMsg("성공적으로 1개가 조회되었습니다.")
                .result(twoThing)
                .build();
    }

    @PutMapping("") //구현 예정
    @Operation(summary = "알림장 수정",
            description = "<strong> 변수명 : notice_id </strong> <p> 알림장 PK ex)1 </p>" +
                    "<strong> 변수명 : title </strong> <p> 수정할 제목 ex)수요일 운동회  </p>" +
                    "<strong> 변수명 : content </strong> <p> 수정할 내용 ex)우천시 실내체험으로 변경 </p>")
    @PreAuthorize("hasRole('TEACHER')")
    public ResultDto<Integer> putNotice(@RequestBody PutNoticeReq p){
        int result=service.putNotice(p);
        return ResultDto.<Integer>builder()
                .statusCode(HttpStatus.OK)
                .resultMsg("성공적으로 수정되었습니다.")
                .result(result)
                .build();
    }

    @DeleteMapping("")
    @Operation(summary = "알림장 삭제",
            description = "<strong> 변수명 : notice_id </strong> <p> 알림장 PK ex)1 </p>" )
    @PreAuthorize("hasRole('TEACHER')")
    public ResultDto<Integer> deleteNotice(@ModelAttribute @ParameterObject DeleteNoticeReq p){
        int result=service.deleteNotice(p);
        return ResultDto.<Integer>builder()
                .statusCode(HttpStatus.OK)
                .resultMsg("성공적으로 삭제했습니다.")
                .result(result)
                .build();
    }
}
