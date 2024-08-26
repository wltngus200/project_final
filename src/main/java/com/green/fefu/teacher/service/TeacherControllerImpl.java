package com.green.fefu.teacher.service;


import com.green.fefu.entity.Teacher;
import com.green.fefu.teacher.model.req.*;
import com.green.fefu.teacher.test.TeacherController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.green.fefu.chcommon.ResponsDataSet.*;

@RestController
@CrossOrigin(origins = "*")
@Slf4j
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
@Tag(name = "선생님 CRUD", description = "선생님 관련 클래스")
public class TeacherControllerImpl implements TeacherController {
    private final TeacherServiceImpl service;

    //    선생님 회원가입
    @PostMapping(value = "/sign-up", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    @Operation(summary = "선생님 회원가입", description = "리턴 => 선생님 PK값")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "teacherPk : \"1\""
            ),
            @ApiResponse(responseCode = "404",
                    description = "에러 난 이유 설명"
            ),
    })
    @Override
    public ResponseEntity CreateTeacher(@RequestBody @Valid CreateTeacherReq p) {
        log.info("CreateTeacher req: {}", p);
        Map map = new HashMap();
        map = service.CreateTeacher(p, map);
        return new ResponseEntity<>(map, OK);
    }

    //    선생님 로그인
    @PostMapping(value = "/sign-in", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    @Operation(summary = "선생님 로그인", description = "리턴 => 이름, 이메일, 담당학급, 엑세스토큰")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description =
                            "<p> name : \"홍길동\"</p>" +
                                    "<p> email : \"test1234@naver.com\"</p>" +
                                    "<p> class : \"1학년 1반\"</p>" +
                                    "<p> accessToken : \"asdasdqwdzxc\"</p>"
            ),
            @ApiResponse(responseCode = "404",
                    description = "에러 난 이유 설명"
            )
    })
    @Override
    public ResponseEntity LogInTeacher(@RequestBody @Valid LogInTeacherReq p, HttpServletResponse res) {
        log.info("LogInTeacher req: {}", p);
        Map map = new HashMap();
        map = service.LogInTeacher(p, map, res);
        return new ResponseEntity<>(map, OK);
    }

    //    선생님 중복확인 ( 아이디, 이메일 )
    @GetMapping(value = "duplicate", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    @Operation(summary = "선생님 아이디 or 이메일 중복확인", description = "리턴 => 없음 <br><strong>아이디 이메일 둘중 하나만 넣어주세요</strong>")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description =
                            "리턴값 없음!"
            ),
            @ApiResponse(responseCode = "404",
                    description = "에러 난 이유 설명"
            )
    })
    @Override
    public ResponseEntity CheckDuplicate(@ParameterObject @ModelAttribute @Valid CheckDuplicateReq p) {
        log.info("CheckDuplicate req: {}", p);

        service.CheckDuplicate(p);

        return new ResponseEntity<>(OK);
    }


    //    선생님 아이디 찾기
    @GetMapping(value = "find_id", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    @Operation(summary = "선생님 아이디 찾기", description = "리턴 => 선생님 ID값")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description =
                            "id : \"test1234\""
            ),
            @ApiResponse(responseCode = "404",
                    description = "에러 난 이유 설명"
            )
    })
    @Override
    public ResponseEntity FindTeacherId(@ParameterObject @ModelAttribute @Valid FindTeacherIdReq p) {
        log.info("FindTeacherId req: {}", p);
        Map map = new HashMap();
        service.FindTeacherId(p, map);
        return new ResponseEntity<>(map, OK);
    }

    //    선생님 비밀번호 찾기
    @GetMapping(value = "find_pwd", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    @Operation(summary = "선생님 비밀번호 찾기 ( 문자 발송 )", description = "리턴 => 랜덤 코드")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description =
                            "randomCode : \"111111\""
            ),
            @ApiResponse(responseCode = "404",
                    description = "에러 난 이유 설명"
            )
    })
    @Override
    public ResponseEntity FindTeacherPassword(@ParameterObject @ModelAttribute @Valid FindTeacherPasswordReq p) {
        log.info("FindTeacherPassword req: {}", p);
        Map map = new HashMap();
        service.FindTeacherPassword(p, map);
        return new ResponseEntity<>(map, OK);
    }

    //    선생님 비밀번호 변경
    @PutMapping(value = "put_pwd", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    @Operation(summary = "선생님 비밀번호 변경", description = "리턴 => 없음")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description =
                            "리턴값 없음!"
            ),
            @ApiResponse(responseCode = "404",
                    description = "에러 난 이유 설명"
            )
    })
    @Override
    public ResponseEntity ChangePassWord(@RequestBody @Valid ChangePassWordReq p) {
        log.info("ChangePassWord req: {}", p);
        Teacher teacher = service.ChangePassWord(p);
        Map map = new HashMap();
        map.put("uid", teacher.getUid()) ;
        return new ResponseEntity<>(map, OK) ;
    }

    //    선생님 내정보 불러오기
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    @Operation(summary = "선생님 내정보 불러오기", description = "리턴 => 아이디, 이름, 전화번호, 이메일, 성별, 담당학급, 생년월일")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description =
                            "{\n" +
                                    "  \"gender\": \"남\",\n" +
                                    "  \"phone\": \"010-0000-0000\",\n" +
                                    "  \"name\": \"홍길동\",\n" +
                                    "  \"birth\": \"2024-06-28\",\n" +
                                    "  \"id\": \"qqqq1234\",\n" +
                                    "  \"addr\": \"12345 # 서울 판교 1234 # 101동\",\n" +
                                    "  \"class\": null,\n" +
                                    "  \"email\": \"213123asd@naver.com\"\n" +
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
    @PreAuthorize("hasRole('TEACHER')")
    @Override
    public ResponseEntity TeacherDetail() {
        Map map = new HashMap();
        service.TeacherDetail(map);
        return new ResponseEntity<>(map, OK);
    }

    //    선생님 정보 변경
    @PatchMapping(produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    @Operation(summary = "선생님 정보 변경", description = "리턴 => 없음")
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
    @PreAuthorize("hasRole('TEACHER')")
    @Override
    public ResponseEntity ChangeTeacher(@RequestBody @Valid ChangeTeacherReq p) {
        log.info("ChangeTeacher req: {}", p);
        service.ChangeTeacher(p);
        return new ResponseEntity<>(OK) ;
    }



//    학생 사진 수정
//    학생 자퇴 처리
//    학생 로그인
}
