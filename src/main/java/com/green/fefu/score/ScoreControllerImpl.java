package com.green.fefu.score;

import com.green.fefu.common.ResultDto;
import com.green.fefu.score.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.green.fefu.common.GlobalConst.ERROR_CODE;
import static com.green.fefu.common.GlobalConst.SUCCESS_CODE;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/Score")
public class ScoreControllerImpl implements ScoreController {

    private final ScoreServiceImpl service;

    @PostMapping
    @Operation(summary = "점수 입력 칸 ")
    @PreAuthorize("hasRole('TEACHER')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description =
                           "statusCode\": null,\n" +
                                   "  \"resultMsg\": null,\n" +
                                   "  \"resultData\": null,\n" +
                                   "  \"code\": 1,\n" +
                                   "  \"msg\": \"점수 입력 성공\",\n" +
                                   "  \"data\": 1\n" +
                                   "}"
            ),
            @ApiResponse(responseCode = "404",
                    description = "에러 난 이유 설명"
            )
    })
    public ResultDto<Long> postScore(@RequestBody InsScoreReq p){
        long res = service.postScore(p);
        try {

            return ResultDto.resultDto(SUCCESS_CODE,"점수 입력 성공",res);
        }catch (RuntimeException e){
            return ResultDto.resultDto1(ERROR_CODE,"점수 입력 실패");
        }
    }
    @GetMapping("/getScore")
    @Operation(summary = "학생성적조회 최초 성적조회 화면 이동시 ")
    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('PARENTS')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description =
                           "\"statusCode\": null,\n" +
                                   "  \"resultMsg\": null,\n" +
                                   "  \"resultData\": null,\n" +
                                   "  \"code\": 1,\n" +
                                   "  \"msg\": \"성적조회성공\",\n" +
                                   "  \"data\": {\n" +
                                   "    \"list\": [\n" +
                                   "      {\n" +
                                   "        \"name\": \"국어\",\n" +
                                   "        \"exam\": 2,\n" +
                                   "        \"mark\": 50,\n" +
                                   "        \"scoreId\": 400,\n" +
                                   "        \"subjectClassRank\": 1,\n" +
                                   "        \"subjectGradeRank\": 1,\n" +
                                   "        \"studentPk\": 2,\n" +
                                   "        \"classAvg\": 34.6,\n" +
                                   "        \"gradeAvg\": 34.6\n" +
                                   "      }\n" +
                                   "    ],\n" +
                                   "    \"studentPk\": 2,\n" +
                                   "    \"latestGrade\": 1,\n" +
                                   "    \"latestSemester\": 1,\n" +
                                   "    \"latestYear\": \"2023\",\n" +
                                   "    \"signResult\": null,\n" +
                                   "    \"classRank\": {\n" +
                                   "      \"classRank\": 5,\n" +
                                   "      \"gradeRank\": 5,\n" +
                                   "      \"classStudentCount\": 7,\n" +
                                   "      \"gradeStudentCount\": 7\n" +
                                   "    }\n" +
                                   "  }\n" +
                                   "}"
            ),
            @ApiResponse(responseCode = "404",
                    description = "에러 난 이유 설명"
            )
    })
    public ResultDto<Dto> getScore(@ParameterObject StuGetRes p){
        try {
            Dto res = service.getScore(p);
            if(res.getList().size() == 0){
                return null;
            }
            return ResultDto.resultDto(SUCCESS_CODE,"성적조회 성공",res);
        }catch (RuntimeException e){
            return ResultDto.resultDto1(ERROR_CODE,"성적조회 실패");
        }
    }
    @GetMapping("/getScoreDetail")
    @Operation(summary = "학생성적조회 학년과 학기 조회시")
    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('PARENTS')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description =
                           "\"name\": \"체육\",\n" +
                                   "        \"exam\": 1,\n" +
                                   "        \"mark\": 95,\n" +
                                   "        \"scoreId\": 324,\n" +
                                   "        \"subjectClassRank\": 2,\n" +
                                   "        \"subjectGradeRank\": 2,\n" +
                                   "        \"studentPk\": 2,\n" +
                                   "        \"classAvg\": 76.25,\n" +
                                   "        \"gradeAvg\": 76.25\n" +
                                   "      }\n" +
                                   "    ],\n" +
                                   "    \"studentPk\": 2,\n" +
                                   "    \"classRank\": {\n" +
                                   "      \"classRank\": 3,\n" +
                                   "      \"gradeRank\": 3,\n" +
                                   "      \"classStudentCount\": 8,\n" +
                                   "      \"gradeStudentCount\": 8\n" +
                                   "    },\n" +
                                   "    \"signResult\": null\n" +
                                   "  }"
            ),
            @ApiResponse(responseCode = "404",
                    description = "에러 난 이유 설명"
            )
    })
    public ResultDto<DtoDetail> getDetailScore(@ParameterObject @ModelAttribute GetDetailScoreReq p){
        try {
            DtoDetail res = service.getDetailScore(p);
            if(res.getList().size() == 0 ){
                return null;
            }
            return ResultDto.resultDto(SUCCESS_CODE,"성적조회성공",res);
        }catch (RuntimeException e){
            e.printStackTrace();
            return ResultDto.resultDto(ERROR_CODE,"성적조회실패",null);
        }
    }
}