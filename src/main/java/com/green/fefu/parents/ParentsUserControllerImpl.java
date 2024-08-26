package com.green.fefu.parents;

import com.green.fefu.entity.ParentOAuth2;
import com.green.fefu.entity.Parents;
import com.green.fefu.entity.Student;
import com.green.fefu.exception.CustomException;
import com.green.fefu.parents.model.*;
import com.green.fefu.security.jwt.JwtTokenProviderV2;
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
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.encrypt.RsaAlgorithm;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.green.fefu.chcommon.ResponsDataSet.OK;
import static com.green.fefu.exception.ljm.LjmErrorCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/parents")
@Slf4j
@Tag(name = "학부모 관련", description = "학부모 CRUD")
public class ParentsUserControllerImpl implements ParentsUserController {
    private final ParentsUserServiceImpl service;
    private final JwtTokenProviderV2 tokenProvider;
    private final String FILE_BASE_PATH = "/home/download/sign/";
    private final String TEST_BASE_PATH = "D:\\download\\2nd\\sign/" ;

    // 학부모 회원가입
    @Override @PostMapping("/sign-up") @Operation(summary = "회원가입", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
            description = "uid: string1234, \n" +
                    " upw: String1234!@#$, \n" +
                    " nm: 강길동, \n" +
                    " phone: 010-6136-4623, \n" +
                    "email: string@naver.com, \n" +
                    " connect: 부, \n" +
                    " studentPk: 2 ")
        }
    )
    public ResponseEntity postParents(@RequestBody PostParentsUserReq p) {
        int result = service.postParentsUser(p);
        return ResponseEntity.ok().body(result);
    }
    // 아이디, 이메일 중복조회
    @Override @GetMapping("/check-duplication") @Operation(summary = "아이디, 이메일 중복조회", description = "리턴 => 없음 <br><strong>아이디 이메일 둘중 하나만 넣어주세요</strong>")
    public ResponseEntity<String> checkEmailOrUid(@ModelAttribute @ParameterObject CheckEmailOrUidReq req) {
        String res = service.checkEmailOrUid(req) ;
        return ResponseEntity.ok().body(res) ;
    }
    // 정보 조회
    @Override @GetMapping("/parent-info") @Operation(summary = "정보조회") @PreAuthorize("hasRole('PARENTS')")
    public ResponseEntity<ParentsUserEntity> getParentsUser(HttpServletRequest req) {
        String token = tokenProvider.resolveToken(req) ;

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build() ;
        }
        ParentsUserEntity p = service.getParentsUser(token);
        log.info("parentsUser: {}", p) ;
        return ResponseEntity.ok().body(p) ;
    }
    // 정보 수정
    @Override @PutMapping("/info-update") @Operation(summary = "정보수정") @PreAuthorize("hasRole('PARENTS')")
    public ResponseEntity<Integer> patchParentsUser(@RequestBody PatchParentsUserReq p) {
        int result = service.patchParentsUser(p) ;
        return ResponseEntity.ok().body(result) ;
    }
    // 학부모 아이디 찾기
    @Override @GetMapping("/find-id") @Operation(summary = "아이디 찾기")
    public ResponseEntity<GetFindIdRes> getFindId(@ModelAttribute @ParameterObject GetFindIdReq p) {
        GetFindIdRes res = service.getFindId(p) ;
        return ResponseEntity.ok().body(res) ;
    }
    // 비밀번호 수정
    @Override @PutMapping("/password-update") @Operation(summary = "비밀번호 수정")
    public ResponseEntity<Integer> patchPassword(@RequestBody PatchPasswordReq p) {
        int result = service.patchPassword(p) ;
        return ResponseEntity.ok().body(result) ;
    }
    // 학부모 로그인
    @Override @PostMapping("/sign-in") @Operation(summary = "로그인")
    public ResponseEntity<SignInPostRes> signInPost(@RequestBody SignInPostReq p, HttpServletResponse res) {
        SignInPostRes postRes = service.signInPost(p, res) ;
        return ResponseEntity.ok().body(postRes) ;
    }
    // 토큰확인
    @Override @GetMapping("/access-token") @Operation(summary = "accessToken - 확인")
    public ResponseEntity<Map> getAccessToken(HttpServletRequest req) {
        Map<String, Object> res = service.getAccessToken(req) ;
        return ResponseEntity.ok().body(res) ;
    }
    // 학부모 비밀번호 찾기
    @Override @GetMapping("/find-password") @Operation(summary = "비밀번호 찾기", description = "문자발송")
    public ResponseEntity getFindPassword(@ModelAttribute @ParameterObject GetFindPasswordReq req) {
        Map map = new HashMap<>() ;
        service.getFindPassword(req, map);
        return new ResponseEntity<>(map, OK) ;
    }
    // 전자서명
    @Override @PostMapping(value = "/signature") @Operation(summary = "전자서명") @PreAuthorize("hasRole('PARENTS')")
    public ResponseEntity signature( @RequestPart MultipartFile pic, @RequestPart SignatureReq req){
        try {
            SignatureRes result = service.signature(pic, req);

            Map map = new HashMap();
            map.put("sign_id", req.getSignId());
            return ResponseEntity.ok().body(result);
        } catch (Exception e){
            e.printStackTrace();
            throw new CustomException(ERROR_SIGNATURE_CODE) ;
        }
    }
    // 학생정보 조회
    @Override @GetMapping("/get-student-parent") @Operation(summary = "자녀 학생정보 조회") @PreAuthorize("hasRole('PARENTS')")
    public ResponseEntity<List<GetStudentParentsRes>> getStudentParents(HttpServletRequest req) {
        String token = tokenProvider.resolveToken(req) ;
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build() ;
        }
        List<GetStudentParentsRes> list = service.getStudentParents(token) ;
        return ResponseEntity.ok().body(list) ;
    }
    // 전사서명 조회
    @GetMapping("/get-signature") @Operation(summary = "전자서명 가져오기" , description = "학부모, 선생님 둘 다 가능") @PreAuthorize("hasRole('PARENTS') or hasRole('TEACHER')")
    public ResponseEntity getSignature(@ModelAttribute @ParameterObject GetSignaturePicReq req){
        try {
            GetSignaturePicRes res = service.getSignaturePics(req);
            String url = String.format("http://112.222.157.156:5121/pic/sign/%s/%s", res.getSignId(), res.getPic()) ;
            res.setPic(url) ;
            return ResponseEntity.ok().body(res);
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException( );
        }
    }
    // 전자서명 다운로드
    @GetMapping("/download/{signPk}") @Operation(summary = "전자서명 다운로드", description = "학부모, 선생님 둘 다 가능") @PreAuthorize("hasRole('PARENTS') or hasRole('TEACHER')")
    public ResponseEntity<UrlResource> downloadFile(@PathVariable Long signPk) {
        try {
            String fileName = service.signatureNm(signPk);
            log.info(fileName) ;
            if (fileName == null || fileName.isEmpty()) {
                throw new RuntimeException("파일을 찾을 수 없습니다.");
            }

            String filePath = FILE_BASE_PATH + signPk + "/" + fileName;
            Path path = Paths.get(filePath);

            UrlResource resource = new UrlResource(path.toUri());
            log.info("resource: {}", resource) ;

            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException("파일을 찾을 수 없거나 읽을 수 없습니다.");
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("파일 다운로드에 실패했습니다." + e.getMessage(), e);
        }
    }
    // 소셜로그인 회원가입
    @PostMapping("/sign-up/social-login") @Operation(summary = "소셜로그인 연동", description = "로그인 이후 소셜 회원가입") @PreAuthorize("hasRole('PARENTS')")
    public ResponseEntity socialPeristalsis(@RequestBody SocialLoginReq req, HttpServletRequest httpServletRequest){
        String token = tokenProvider.resolveToken(httpServletRequest) ;

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build() ;
        }
        ParentOAuth2 result = service.signUpSocialLogin(req, token) ;

        return ResponseEntity.ok().body(result) ;
    }
    // 소셜로그인
    @PostMapping("/sign-in/social-login") @Operation(summary = "소셜로그인", description = "회원가입된 회원이 소셜로그인 연동한 경우에만 로그인 가능")
    public ResponseEntity socialLogin(@Valid @RequestBody SocialSignInReq req, HttpServletResponse res){
        SignInPostRes result = service.socialSignIn(req, res) ;
        return ResponseEntity.ok().body(result) ;
    }
    // 소셜로그인 시 로컬아이디가 없는 경우
    @PostMapping("/sign-up/social-login/random-code") @Operation(summary = "소셜로그인 회원가입", description = "학생 랜덤코드로 회원가입된 학부모가 없으면 회원가입")
    public ResponseEntity socialLoginSignUp(@RequestBody RandCodeReq randCode){
        int result = service.getStudentRandCode(randCode) ;
        return ResponseEntity.ok().body(result) ;
    }
    // 소셜회원가입 시 전화번호 및 관계 주입용
    @PostMapping("/sign-up/social-login/phone") @Operation(summary = "소셜로그인 회원가입 전화번호 및 관계", description = "전화번호 입력")
    public ResponseEntity socialLoginSignUpPhone(@Valid @RequestBody ChangeNumberAndConnect req){
        log.info("Received request: {}", req);
        ChangeNumberAndConnectRes result = service.postSocialPhoneNumber(req) ;
        return ResponseEntity.ok().body(result) ;
    }
    // 소셜회원가입 처음부터 가능
    @PostMapping("/sign-up/social/first") @Operation(summary = "소셜회원가입 다이렉트로 하기.")
    public ResponseEntity socialSignUp(@RequestBody SocialLoginSIgnUpReq req){
        int result = service.socialSignUpLogin(req) ;
        return ResponseEntity.ok().body(result) ;
    }

    @GetMapping(value = "detail", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    @Operation(summary = "선생 pk로 담당 학급의 부모님 조회 ")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity getParentList() {
        List<GetParentRes> result = new ArrayList<>();
        result = service.getParentList(result);
        return new ResponseEntity<>(result, OK);
    }
}
