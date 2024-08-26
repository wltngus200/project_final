package com.green.fefu.calender;

import com.green.fefu.calender.req.CalenderReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.green.fefu.chcommon.ResponsDataSet.OK;

@RestController
@CrossOrigin(origins = "*")
@Slf4j
@RequestMapping("/api/calender")
@RequiredArgsConstructor
@Tag(name = "일정 CRUD", description = "일정 관련 API")
public class CalenderController {
    private final CalenderService calenderService;

    @PostMapping
    @Operation(summary = "일정 추가", description = "calenderType\": \"1->수다날 2->시험일 3->휴무일\"")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "입력 성공"
            ),
            @ApiResponse(responseCode = "404",
                    description = "에러 난 이유 설명"
            ),
    })
    public ResponseEntity postCalender(@RequestBody @Valid CalenderReq p) {
        calenderService.postCalender(p);
        return new ResponseEntity<>("입력 성공",OK);
    }


    @GetMapping
    @Operation(summary = "일정 불러오기", description = "리턴 => 일정 pk, 행사 일정, 행사 명, 행사 타입")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "teacherPk : \"1\""
            ),
            @ApiResponse(responseCode = "404",
                    description = "에러 난 이유 설명"
            ),
    })
    public ResponseEntity getCalenderList() {
        List<Map> result = calenderService.getCalenderList();
        return new ResponseEntity<>(result, OK);
    }
}
