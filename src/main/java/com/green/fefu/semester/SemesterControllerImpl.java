package com.green.fefu.semester;

import com.green.fefu.common.ResultDto;
import com.green.fefu.semester.model.SemesterReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.green.fefu.common.GlobalConst.ERROR_CODE;
import static com.green.fefu.common.GlobalConst.SUCCESS_CODE;



@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/semester")
public class SemesterControllerImpl implements SemesterController {
    private final SemesterService service;

    @PostMapping("/post")
    @Operation(summary = "학기 등록 ")
    @ApiResponse(description = "1: 성공 , -1 : 실패")
    public ResultDto<Integer> postSemester(@RequestBody SemesterReq p){
       try {
           int res = service.postSemester(p);
           return ResultDto.resultDto(SUCCESS_CODE,"성공",res);
       }catch (RuntimeException e){
           return ResultDto.resultDto1(ERROR_CODE,"학기등록실패");
       }
    }
}
