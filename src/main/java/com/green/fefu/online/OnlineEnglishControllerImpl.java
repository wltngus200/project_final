package com.green.fefu.online;

import com.green.fefu.common.model.ResultDto;
import com.green.fefu.online.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/online/english")
@Tag(name = "온라인 학습-영어", description = "영어 단어, 듣기 문제 CRUD")
@Slf4j
public class OnlineEnglishControllerImpl {
    private final OnlineEnglishServiceImpl service;

    @PostMapping("/words")
    @Operation(summary = "영어 단어 문제 등록", description = ""+
            "<p><strong>word</strong> 영어 단어 ex. computer</p>" +
            "<p><strong>answer</strong> 한국어 의미 ex.컴퓨터</p>")
    @PreAuthorize("hasRole('TEACHER')")
    public ResultDto<Integer> postEnglishWordQuestion(@RequestPart PostOnlineQuestionEnglishWordReq p, @RequestPart(required = false) MultipartFile pic){
        log.info("controller - p {}", p);
        log.info("controller - pic {}", pic);
        int result=service.postEnglishWordQuestion(p,pic);
        return ResultDto.<Integer>builder()
                .result(result)
                .statusCode(HttpStatus.OK)
                .resultMsg(HttpStatus.OK.toString())
                .build();
    }

    @PostMapping("/listening")
    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "영어 듣기 문제 등록", description = ""+
            "<p><strong>question</strong> 문제 ex. Amy가 바자회에 제출할 품목은 무엇입니까?</p>" +
            "<p><strong>answer</strong> 정답 단답형 ex.곰인형</p>"+
            "<p><strong>sentence</strong> ex. I love teddy bear</p>")
    public ResultDto<Integer> postEnglishListeningQuestion(@RequestPart PostOnlineQuestionEnglishListeningReq p, @RequestPart(required = false) MultipartFile pic){
        log.info("controller - p {}", p);
        log.info("controller - pic {}", pic);
        int result=service.PostEnglishListeningQuestion(p,pic);
        return ResultDto.<Integer>builder()
                .result(result)
                .statusCode(HttpStatus.OK)
                .resultMsg(HttpStatus.OK.toString())
                .build();
    }

    @GetMapping("/words")
    @Operation(summary = "단어 리스트 불러오기", description = "" +
            "<p><strong>studentPk</strong> 학생 1명의 PK</p>" +
            "<p>학생, 학부모, 선생 모두 사용 가능</p>" +
            "<p>학부모의 경우 한 학생의 pk를 <strong>무조건!!</strong> 보내줘야함</p>")
    public ResultDto<List<GetEnglishWordQuestionRes>> getEnglishWords(@ParameterObject @ModelAttribute GetEnglishQuestionReq p){
        log.info("controller - p {}", p);
        List<GetEnglishWordQuestionRes> list=service.getEnglishWords(p);
        return ResultDto.<List<GetEnglishWordQuestionRes>>builder()
                .statusCode(HttpStatus.OK)
                .resultMsg(HttpStatus.OK.toString())
                .result(list)
                .build();

    }

    @GetMapping("/listening")
    @Operation(summary = "듣기 문제 리스트 불러오기", description = "" +
            "<p><strong>studentPk</strong> 학생 1명의 PK</p>" +
            "<p>학생, 학부모, 선생 모두 사용 가능</p>" +
            "<p>학부모의 경우 한 학생의 pk를 <strong>무조건!!</strong> 보내줘야함</p>")
    public ResultDto<List<GetEnglishListeningQuestionRes>> getEnglishListening(@ParameterObject @ModelAttribute GetEnglishQuestionReq p){
        log.info("controller - p {}", p);
        List<GetEnglishListeningQuestionRes> list=service.getEnglishListening(p);
        return ResultDto.<List<GetEnglishListeningQuestionRes>>builder()
                .statusCode(HttpStatus.OK)
                .resultMsg(HttpStatus.OK.toString())
                .result(list)
                .build();
    }
}
