package com.green.fefu.admin.service;


import com.green.fefu.admin.model.req.FindUnAcceptListReq;
import com.green.fefu.admin.model.req.FindUserListReq;
import com.green.fefu.admin.model.req.UpdateUserReq;
import com.green.fefu.admin.model.req.adminUserReq;
import com.green.fefu.admin.test.AdminController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.green.fefu.chcommon.ResponsDataSet.*;

@RestController
@CrossOrigin(origins = "*")
@Slf4j
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "관리자 승인", description = "관리자 관련 클래스")
public class AdminControllerImpl implements AdminController {
    private final AdminServiceImpl service;

    //    list Get
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")

    @Operation(summary = "회원 가입 시 승인/반려 처리를 위한 유저 List 조회", description = "리턴 => 학부모, 승인 대기 학부모 리스트" +
            "승인 신청일, 자녀 학년, 자녀 학급, 부모 pk, 부모 id, 부모 이름" +
            "<hr/>" +
            "<p> p = > 학부모 교직원 분류 코드 ( 1 -> 학부모 / 2 -> 교직원 ) </p>" +
            "<p> searchWord = > 검색어 입력(이름) ( 필수값 아님! )</p>")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "{\n" +
                            "  \"code\": \"학부모\",\n" +
                            "  \"userList\": [\n" +
                            "    {\n" +
                            "      \"createdAt\": \"2024-07-03 15:55:46\",\n" +
                            "      \"grade\": \"1학년\",\n" +
                            "      \"name\": \"부모\",\n" +
                            "      \"pk\": \"1\",\n" +
                            "      \"id\": \"test1234\",\n" +
                            "      \"class\": \"1반\"\n" +
                            "    },\n" +
                            "  ]\n" +
                            "}"
            ),
            @ApiResponse(responseCode = "404",
                    description = "에러 난 이유 설명"
            ),
            @ApiResponse(responseCode = "401",
                    description = "JWT ACCESSTOKEN 에러 ( 토큰을 헤더에 추가해 주세요 )"
            ),
            @ApiResponse(responseCode = "403",
                    description = "해당 유저는 사용 권한이 없음"
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity findUnAcceptList(@ParameterObject @ModelAttribute FindUnAcceptListReq p) {
        log.info("parameter p: {}", p);
        Map map = new HashMap();
        map = service.findUnAcceptList(p, map);
        return new ResponseEntity<>(map, OK);
    }


    //    반려
    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    @Operation(summary = "유저 회원가입 반려", description = "리턴 => 없음")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "리턴값 없음!"
            ),
            @ApiResponse(responseCode = "404",
                    description = "에러 난 이유 설명"
            ),
            @ApiResponse(responseCode = "401",
                    description = "JWT ACCESSTOKEN 에러 ( 토큰을 헤더에 추가해 주세요 )"
            ),
            @ApiResponse(responseCode = "403",
                    description = "해당 유저는 사용 권한이 없음"
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity deleteUser(@ParameterObject @ModelAttribute @Valid adminUserReq p) {
        log.info("deleteUser req : {}", p);
        service.deleteUser(p);
        return new ResponseEntity<>(OK);
    }


    //    승인
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    @Operation(summary = "유저 회원가입 승인", description = "리턴 => 없음")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "리던값 없음!"
            ),
            @ApiResponse(responseCode = "404",
                    description = "에러 난 이유 설명"
            ),
            @ApiResponse(responseCode = "401",
                    description = "JWT ACCESSTOKEN 에러 ( 토큰을 헤더에 추가해 주세요 )"
            ),
            @ApiResponse(responseCode = "403",
                    description = "해당 유저는 사용 권한이 없음"
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity acceptUser(@ParameterObject @ModelAttribute @Valid adminUserReq p) {
        log.info("acceptUser req : {}", p);
        service.acceptUser(p);
        return new ResponseEntity<>(OK);
    }

//    검색 기능 ( 학부모, 교직원 )
    @GetMapping(value = "list",produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    @Operation(summary = "교직원 학부모 현황 조회 ( 재직자 / 퇴사자 or 활성화 / 비활성화 )", description = "교직원, 학부모 등의 상태 리스트 불러오는 api" +
            "<hr/>" +
            "            <p> p = > 학부모 교직원 분류 코드 ( 1 -> 학부모 / 2 -> 교직원 ) </p>" +
            "            <p> searchWord = > 검색어 입력(이름) ( 필수값 아님! )</p>" +
            "<p>check = > 퇴사자 포함 체크박스 여부 ( 1 -> 체크 안됨 / 2 -> 체크 됨 )</p>")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "\"createdAt\": \"가입일\",\n" +
                            "    \"name\": \"학부모/선생 이름\",\n" +
                            "    \"studentList\": (학부모 만)[\n" +
                            "      {\n" +
                            "        \"studentId\": \"학생 아이디\",\n" +
                            "        \"studentPK\": 학생 pk,\n" +
                            "        \"studentGrade\": \"학생 학년\",\n" +
                            "        \"studentCode\": \"학생 코드\",\n" +
                            "        \"studentName\": \"학생 이름\",\n" +
                            "        \"studentState\": \"학생 상태\",\n" +
                            "        \"studentClass\": \"학생 학급\"\n" +
                            "      },\n" +
                            "    ],\n" +
                            "    \"state\": \"학부모/선생 상태\",\n" +
                            "    \"id\": \"학부모/선생 아이디\",\n" +
                            "    \"pk\": 학부모/선생 pk,\n" +
                            "    \"totalChild\": (학부모 만)학교에 다닌 총 자녀 수,\n" +
                            "    \"inSchoolStudent\": (학부모 만)현재 학교에 재학중인 자녀 수\n" +
                            "    \"totalStudent\": (교직원 만)담당학급 학생 수,"+
                            "  }"
            ),
            @ApiResponse(responseCode = "404",
                    description = "에러 난 이유 설명"
            ),
            @ApiResponse(responseCode = "401",
                    description = "JWT ACCESSTOKEN 에러 ( 토큰을 헤더에 추가해 주세요 )"
            ),
            @ApiResponse(responseCode = "403",
                    description = "해당 유저는 사용 권한이 없음"
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity findUserList(@ParameterObject @ModelAttribute @Valid FindUserListReq p) {
        List<Map<String,Object>> list = new ArrayList();
        list = service.findUserList(p, list);
        return new ResponseEntity<>(list, OK);
    }
//    학적 변동 처리
    @PatchMapping
    @Operation(summary = "학부모/교직원 -> 재직중/퇴사 or 활성화/비활성화 처리", description = "<p>p : 1-> 부모 / 2-> 교직원 / 3-> 학생(필수 값)</p>" +
            "<p>pk : 학부모/교직원/학생 pk값</p>" +
            "<p>state : 학부모/교직원/학생 변환할 상태 값</p>" +
            "<hr/>" +
            "<strong>state 값 (학생, 부모, 선생) 순서임</strong>" +
            "<p> 1 -> 재학,활성화,재직 </p>" +
            "<p> 2 -> 전학,비활성화,퇴사 </p>" +
            "<p> 3 -> 졸업 </p>" +
            "<p> 4 -> 퇴학 </p>" +
            "<hr/>" +
            "<p>userName : 유저 이름 변경</p>" +
            "<p>userGrade : (선생) 담당 학년 / </p><p>(학생) 학년 <strong>학년이나 학급 을 바꿀때는 꼭!! 학년학급 둘다 입력 해주세요</strong></p>" +
            "<p>userClass : (선생) 담당 학급 / </p><p>(학생) 학급 <strong>학년이나 학급 을 바꿀때는 꼭!! 학년학급 둘다 입력 해주세요</strong></p>")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "리턴값 없음"
            ),
            @ApiResponse(responseCode = "404",
                    description = "에러 난 이유 설명"
            ),
            @ApiResponse(responseCode = "401",
                    description = "JWT ACCESSTOKEN 에러 ( 토큰을 헤더에 추가해 주세요 )"
            ),
            @ApiResponse(responseCode = "403",
                    description = "해당 유저는 사용 권한이 없음"
            )
    })
    public ResponseEntity updateUser(@RequestBody @Valid UpdateUserReq p) {
        log.info("UpdateUserReq:{}", p);
        service.updateUser(p);
        return new ResponseEntity<>(OK);
    }

}
