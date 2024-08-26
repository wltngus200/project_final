package com.green.fefu.online;

import com.green.fefu.common.model.ResultDto;
import com.green.fefu.online.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/online")
@Tag(name = "온라인 학습-국어, 수학", description = "국어, 수학문제 CRUD")
public class HaesolOnlineControllerImpl {
    private final HaesolOnlineServiceImpl service;

    @Operation(summary = "국어 및 수학 문제 등록", description = "" +
            "<p><strong>subjectCode</strong> (정수)과목 코드 1: 국어 2: 수학</p>" +
            "<p><strong>typeTag</strong> (정수)세부 과목 ex. 문법, 사칙연산, 규칙찾기</p>" +
            "<div>" +
            "<blockquote>"+
            "<p>    11 -> 독해</p>" +
            "<p>    12 -> 문법</p>"  +
            "<p>    13 -> 문학</p>" +
            "<hr>"+
            "<p>    21 -> 사칙연산</p>" +
            "<p>    22 -> 단위환산</p>" +
            "<p>    23 -> 그래프</p>" +
            "<p>    24 -> 규칙찾기</p>" +
            "<p>    25 -> 도형 넓이 계산</p>" +
            "</div>"+
            "<p><strong>queTag</strong> (정수)문제 유형 1: 객관식 2: 주관식 (Default 1)</p>" +
            "<p><strong>level</strong> (정수)난이도 1~5</p>" +
            "<p><strong>question</strong> (문자열)문제 ex. 다음 중 옳은 것을 고르시오</p>" +
            "<p><strong>content</strong> (문자열)내용 ex. 먼 훗날 당신이 찾으시면 그때에 내 말이 \"잊었노라\"</p>" +
            "<p><strong>multiple</strong> (문자열 리스트)보기 리스트 사과 딸기 바나나 청포도 배(숫자 없이 문자만)</p>" +
            "<p><strong>answer</strong> (정수)정답이 되는 보기 번호 ex.1" +
            "<p><strong>explanation</strong> (문자열)문제에 대한 해설")
    @PostMapping("/question")
    @PreAuthorize("hasRole('TEACHER')")
    public ResultDto<Integer> PostKorAMatQuestion(@RequestPart(required = false) MultipartFile pic, @RequestPart PostOnlineQuestionReq p){
        log.info("Controller 데이터 객체 : {}", p);
        int result=service.PostKorAMatQuestion(pic, p);
        return ResultDto.<Integer>builder()
                .statusCode(HttpStatus.OK)
                .result(result)
                .resultMsg(HttpStatus.OK.toString())
                .build();
    }


    @GetMapping("/test")
    @Operation(summary = "문제 리스트 불러오기", description = "" +
            "<p>학생, 학부모, 선생 모두 사용 가능</p>" +
            "<p>학부모의 경우 한 학생의 pk를 <strong>무조건!!</strong> 보내줘야함</p>" +
            "<hr>" +
            "<p><strong>studentPk</strong> 학생 1명의 PK</p>" +
            "<p><strong>subjectCode</strong> 국어/수학 과목 선택</p>" +
            "<hr>" +
            "<p><strong>queId </strong>문제의 PK</p>" +
            "<p><strong>question </strong>문제(ex.~것을 고르시오.)</p>" +
            "<p><strong>level </strong>난이도 1~5</p>" +
            "<p><strong>queTag </strong>문제 타입 1->객관식 2-> 주관식</p>" +
            "<p><strong>contents </strong>문제 내용(ex.민수: 오늘 하늘이 참 맑다)</p>" +
            "<p><strong>pic </strong>사진 이름</p>")
    public ResponseEntity GetKorAMatQuestion(@ParameterObject GetKoreanAndMathQuestionReq p) {
        List<GetKoreanAndMathQuestionRes> list=service.GetKorAMatQuestion(p);

        log.info("컨트롤러 리턴");
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary="학생 마킹 값 전달 및 저장", description = "" +
            "<p><strong>중요!! 스웨거 테스트시에는 숫자 리스트의 쌍따옴표를 없애주세요 </strong>예시 [1,5,8,3,9,2]</p>" +
            "<p>문제의 PK 번호와 학생이 마킹한 번호 리스트 제공 부탁드립니다</p>" +
            "<hr>" +
            "<p><strong>questionPk </strong> 현재 출력된 문제의 PK값</p>" +
            "<p><strong>omrAnswer </strong> 학생이 제출한 OMR 마킹</p>" +
            "<p><strong>title </strong> 온라인 시험의 이름</p>" +
            "<p><strong>subjectCode </strong> 1->국어 2->수학</p>" +
            "<hr>" +
            "<p><strong>questionPk </strong> 현재 출력된 문제의 PK값</p>" +
            "<p><strong>omrAnswer </strong> 학생이 제출한 OMR 마킹</p>" +
            "<p><strong>title </strong> 온라인 시험의 이름</p>" +
            "<p><strong>subjectCode </strong> 1->국어 2->수학</p>" +
            "<p><strong>realAnswer </strong> 실제 정답</p>" +
            "<p><strong>typeString </strong>문제유형 ex. 문법, 도형넓이 ...</p>")
    public ResponseEntity testMarking(@RequestBody StudentOmr p){
        log.info("처음 받아온 p:{}",p);
        TestOutCome outCome=service.testMarking(p);
        log.info("서비스에 들어간:{}",p);
        log.info("리턴 값:{}", outCome);
        return new ResponseEntity(outCome, HttpStatus.OK);
    }

    @GetMapping("/recode")
    @Operation(summary="오답노트 목록 가져오기", description = "" +
            "<p>별도의 입력값 필요 없음</p>" +
            "<hr>" +
            "<p><strong>recodePk</strong> 오답노트 Pk</p>" +
            "<p><strong>title</strong> 오답노트의 제목</p>" +
            "<p><strong>createdAt</strong> 시험 일자</p>" +
            "<p><strong>subject</strong> 과목</p>")
    public ResponseEntity testRecode(){
         List<OnlineTestRecordListRes> list=service.testRecode();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/recode/{recodePk}")
    @Operation(summary="오답노트 pk값에 따른 오답노트 기록", description = "" +
            "<p><strong>recodePk</strong> recode의 PK값</p>" +
            "<hr>" +
            "<p><strong></strong>  Pk</p>" +
            "<p><strong></strong> </p>" +
            "<p><strong></strong> </p>" +
            "<p><strong></strong> </p>")
    public ResponseEntity testQuestion(@PathVariable long recodePk){
    //여기
        return new ResponseEntity(1, HttpStatus.OK);
    }

    @DeleteMapping("/test/delete/{questionPk}")
    @Operation(summary = "(FE요청)pk값만으로 문제 지우기")
    public ResponseEntity tmpDeleteQuestion(@PathVariable long questionPk){
        int result=service.tmpDeleteQuestion(questionPk);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @GetMapping("/test/get/{grade}")
    @Operation(summary = "(FE요청)학년별 문제 조회")
    public ResponseEntity tmpGetQuestion(@PathVariable long grade){
        List<GetKoreanAndMathQuestionRes> list=service.tmpGetQuestion(grade);
        return new ResponseEntity(list, HttpStatus.OK);
    }

}
