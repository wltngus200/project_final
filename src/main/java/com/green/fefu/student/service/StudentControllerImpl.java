package com.green.fefu.student.service;

import com.green.fefu.parents.model.ParentsUser;
import com.green.fefu.student.model.dto.getListForNoParent;
import com.green.fefu.student.model.dto.getStudent;
import com.green.fefu.student.model.req.*;
import com.green.fefu.student.test.StudentController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static com.green.fefu.chcommon.ResponsDataSet.*;

@RestController
@CrossOrigin(origins = "*")
@Slf4j
@RequestMapping("/api/student")
@RequiredArgsConstructor
@Tag(name = "학생 CRUD", description = "학생 관련 클래스")
public class StudentControllerImpl implements StudentController {
    private final StudentServiceImpl service;

    //    학생 생성
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    @Operation(summary = "학생 생성 프론트 사용 XXXXX (백에서 데이터 넣을려고 만듦)", description = "리턴 => 학생 PK")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "studentPk : \"1\""
            ),
            @ApiResponse(responseCode = "404",
                    description = "에러 난 이유 설명"
            ),
    })
    @Override
    public ResponseEntity createStudent(@RequestPart @Valid createStudentReq p, @RequestPart(required = false) MultipartFile pic) {
        log.info("createStudent req : {}", p);
        Map map = new HashMap();
        map = service.createStudent(p, pic, map);
        return new ResponseEntity<>(map, OK);
    }

    //    학생 삭제??? ( 데이터 존재 but, 삭제 X ) -> 전학, 졸업 등등
    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    @Operation(summary = "학생 삭제", description = "리턴 => 없음")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "리턴값 없음!"
            ),
            @ApiResponse(responseCode = "404",
                    description = "에러 난 이유 설명"
            ),
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @Override
    public ResponseEntity deleteStudent(@ParameterObject @ModelAttribute @Valid deleteStudentReq p) {
        log.info("deleteStudent req : {}", p);
        service.deleteStudent(p);
        return new ResponseEntity<>(OK);
    }

    //    선생기준 -> 자기 반 학생 리스트 들고오기
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    @Operation(summary = "담당 학급의 학생 List", description = "리턴 => 이름, 성별, 생년월일, 전화번호, 부모 이름, 부모 전화번호")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "[" +
                            "{\n" +
                            "    \"studentPk\": 2,\n" +
                            "    \"num\": 1,\n" +
                            "    \"name\": \"이학생\",\n" +
                            "    \"gender\": \"여\",\n" +
                            "    \"birth\": \"2010-07-20\",\n" +
                            "    \"phone\": \"010-2222-3333\",\n" +
                            "    \"parentName\": \"이부모\",\n" +
                            "    \"parentPhone\": \"010-2345-6789\"\n" +
                            "  }" +
                            "]"
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
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    @Override
    public ResponseEntity getStudentList() {
        List<getStudent> result = new ArrayList<>();
        result = service.getStudentList(result);
        return new ResponseEntity<>(result, OK);
    }



    //    선생기준 -> 자기 반 학생 한명 전체 데이터 들고오기
    @GetMapping(value = "detail", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    @Operation(summary = "학생 한명의 정보 가져오기", description = "리턴 => 생년월일, 관계, 선생이름," +
            " 생성일, 주소, 우편번호, 사진, 이름, 전화번호, 성별, 학년 학급, 기타사항, pk, 부모 아이디, 부모 이름, 부모 전화번호")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "{\n" +
                            "  \"studentBirth\": \"2010-05-15\",\n" +
                            "  \"connet\": \"부\",\n" +
                            "  \"teacherName\": \"정선생\",\n" +
                            "  \"studentAddr\": \"서울 판교로 112\",\n" +
                            "  \"parentId\": \"parent1\",\n" +
                            "  \"parentPhone\": \"010-1234-5678\",\n" +
                            "  \"studentClass\": \"2학년 01반\",\n" +
                            "  \"studentEtc\": \"갑각류 알러지 있음\",\n" +
                            "  \"studentPk\": \"1\",\n" +
                            "  \"studentZoneCode\": \"1234\",\n" +
                            "  \"parentName\": \"김부모\",\n" +
                            "  \"studentGender\": \"남\",\n" +
                            "  \"studentName\": \"홍길동\",\n" +
                            "  \"studentPic\": \"47872175-b41f-4080-bcf9-dc72604c46d5.png\",\n" +
                            "  \"studentPhone\": \"010-0000-0000\",\n" +
                            "  \"prevEtcList\": [\n" +
                            "    {\n" +
                            "      \"etc\": null,\n" +
                            "      \"teacherName\": \"홍길동\",\n" +
                            "      \"uclass\": \"1학년 01반\"\n" +
                            "    }\n" +
                            "  ],\n" +
                            "  \"studentCreatedAt\": \"2024-07-04 08:02:22\",\n" +
                            "  \"studentDetail\": \"\"\n" +
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
    @PreAuthorize("hasRole('PARENTS') or hasRole('TEACHER') or hasRole('STUDENT')")
    @Override
    public ResponseEntity getStudentDetail(@RequestParam long pk) {
        log.info("getStudentDetail req : {}", pk);
        Map map = new HashMap();
        map = service.getStudentDetail(pk, map);
        return new ResponseEntity<>(map, OK);
    }

    //    선생기준 -> 학생 정보 수정
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    @Operation(summary = "학생 한명의 정보 수정", description = "리턴 => 없음")
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
    @PreAuthorize("hasRole('TEACHER') or hasRole('PARENTS')")
    @Override
    public ResponseEntity updateStudent(@RequestPart(required = false) @Valid updateStudentReq p, @RequestPart(required = false) MultipartFile pic) {
        log.info("updateStudent req : {}", p);
        service.updateStudent(p, pic);
        return new ResponseEntity<>(OK);
    }


    //    부모 회원가입시 -> 이름 기준 검색 -> 학생 LIST 불러오기 ( 이름 + 전화번호 + 사진 + 학년 + 반 )
    @GetMapping(value = "list", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    @Operation(summary = "부모 회원가입 시 학생 코드 입력", description = "리턴 => 없음")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = ""
            ),
            @ApiResponse(responseCode = "404",
                    description = "에러 난 이유 설명"
            )
    })
    @Override
    public ResponseEntity getStudentListForParent(@RequestParam String searchWord) {
        log.info("{}", searchWord);
        Map response = service.getStudentListForParent(searchWord);

        return new ResponseEntity<>(response, OK);
    }

    @PatchMapping(produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    @Operation(summary = "학생 학년 증가", description = "리턴 => 없음")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "리턴 값 없음!"
            ),
            @ApiResponse(responseCode = "404",
                    description = "에러 난 이유 설명"
            )
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity studentAdvanceGrade(@RequestBody @Valid List<studentAdvanceGradeReq> p) {
        log.info("studentAdvanceGrade req : {}", p);
        service.studentAdvanceGrade(p);
        return new ResponseEntity<>(OK);
    }

    @PostMapping(value = "sign-in", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    @Operation(summary = "학생 로그인", description = "리턴 => ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = ""
            ),
            @ApiResponse(responseCode = "404",
                    description = "에러 난 이유 설명"
            )
    })
    public ResponseEntity studentSignIn(@RequestBody @Valid StudentSignInReq p, HttpServletResponse res) {
        log.info("StudentSignInReq : {}", p);
        Map map = service.studentSignIn(p, res);
        return new ResponseEntity<>(map, OK);
    }


    @PutMapping(value = "child", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    @Operation(summary = "학부모 학생 추가 (학부모 로그인 필요함)", description = "리턴 => 없음")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "성공"
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
    @PreAuthorize("hasRole('PARENTS')")
    public ResponseEntity addChild(@ParameterObject @ModelAttribute @Valid AddChild p) {
        log.info("addChild req : {}", p);
        service.addChild(p);
        Map map = new HashMap();
        map.put("result", "성공");
        return new ResponseEntity<>(map, OK);
    }

}
