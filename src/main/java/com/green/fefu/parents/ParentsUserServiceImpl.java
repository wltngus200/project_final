package com.green.fefu.parents;

import com.green.fefu.chcommon.Parser;
import com.green.fefu.chcommon.SmsSender;
import com.green.fefu.common.AppProperties;
import com.green.fefu.common.CookieUtils;
import com.green.fefu.common.CustomFileUtils;
import com.green.fefu.entity.ParentOAuth2;
import com.green.fefu.entity.Parents;
import com.green.fefu.entity.ScoreSign;
import com.green.fefu.entity.Student;
import com.green.fefu.exception.CustomException;
import com.green.fefu.parents.model.*;
import com.green.fefu.parents.repository.ParentOAuth2Repository;
import com.green.fefu.parents.repository.ParentRepository;
import com.green.fefu.parents.repository.ScoreSignRepository;
import com.green.fefu.security.AuthenticationFacade;
import com.green.fefu.security.MyUser;
import com.green.fefu.security.MyUserDetails;
import com.green.fefu.security.SignInProviderType;
import com.green.fefu.security.jwt.JwtTokenProviderV2;
import com.green.fefu.sms.SmsService;
import com.green.fefu.student.repository.StudentRepository;
import com.green.fefu.student.service.StudentMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.regex.Pattern;

import static com.green.fefu.exception.bch.BchErrorCode.PASSWORD_NO_MATCH_ERROR;
import static com.green.fefu.exception.bch.BchErrorCode.QUERY_RESULT_ERROR;
import static com.green.fefu.exception.ljm.LjmErrorCode.*;
import static com.green.fefu.teacher.model.dataset.ExceptionMsgDataSet.DUPLICATE_DATA_ERROR;
import static com.green.fefu.teacher.model.dataset.ExceptionMsgDataSet.SMS_SEND_ERROR;


@Service
@Slf4j
@RequiredArgsConstructor
public class ParentsUserServiceImpl implements ParentsUserService {
    private final ParentsUserMapper mapper ;
    private final CustomFileUtils customFileUtils ;
    private final PasswordEncoder passwordEncoder ;
    private final JwtTokenProviderV2 jwtTokenProvider ;
    private final CookieUtils cookieUtils ;
    private final AuthenticationFacade authenticationFacade ;
    private final AppProperties appProperties ;
    private final String ID_PATTERN = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{6,12}$" ;
    private final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d|.*[!@#$%^&*()\\-_=+\\\\|\\[{\\]};:'\",<.>/?]).{8,20}$" ;
    private final Pattern idPattern = Pattern.compile(ID_PATTERN) ;
    private final Pattern passwordPattern = Pattern.compile(PASSWORD_PATTERN) ;
    private final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$" ;
    private final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN) ;
    private final SmsService smsService ;
    private final StudentMapper studentMapper ;
    private final ParentRepository repository ;
    private final StudentRepository studentRepository ;
    private final ParentOAuth2Repository oAuth2Repository ;
    private final ParentOAuth2Repository parentOAuth2Repository;
    private final ScoreSignRepository scoreSignRepository ;
    @Value("${file.directory}") String uploadPath ;
    @Value("${coolsms.api.caller}") private String coolsmsApiCaller;

    @Override @Transactional // 회원가입
    public int postParentsUser(PostParentsUserReq p) {
        if(p.getUid() == null || p.getUpw() == null){
            throw new CustomException(ESSENTIAL_INPUT_MATTERS) ;
        } else if (!idPattern.matcher(p.getUid()).matches()) {
            throw new CustomException(ID_PATTERN_ERROR) ;
        } else if (!passwordPattern.matcher(p.getUpw()).matches()) {
            throw new CustomException(PASSWORD_PATTERN_ERROR) ;
        } else if (p.getEmail() != null && !p.getEmail().isEmpty()) {
            if(!emailPattern.matcher(p.getEmail()).matches()) {
                throw new CustomException(EMAIL_PATTERN_ERROR);
            }
        }
        if ( p.getAddr() != null && p.getZoneCode() != null ){
            p.setAddrs(p.getZoneCode(), p.getAddr(), p.getDetail()) ;
        }
        String password = passwordEncoder.encode(p.getUpw());

        Parents parents = new Parents() ;
        parents.setUid(p.getUid()) ;
        parents.setUpw(password) ;
        parents.setName(p.getNm()) ;
        parents.setPhone(p.getPhone()) ;
        parents.setSubPhone(p.getSubPhone()) ;
        System.out.println(p.getEmail());
        parents.setEmail(p.getEmail()) ;
        parents.setConnect(p.getConnect()) ;
        parents.setAddr(p.getAddrs()) ;
        parents.setAuth("ROLE_PARENTS") ;

        repository.save(parents) ;

        ParentOAuth2 parentOAuth2 = new ParentOAuth2() ;
        parentOAuth2.setName(p.getNm());
        parentOAuth2.setEmail(p.getEmail()) ;
        parentOAuth2.setParentsId(parents) ;
        parentOAuth2.setProviderType(SignInProviderType.LOCAL);

        parentOAuth2Repository.save(parentOAuth2) ;

        int stuResult = studentMapper.updStudentParent( p.getStudentRandomCode(), parents.getParentsId());
        if(stuResult != 1){
            throw new CustomException(STUDENT_INFORMATION_INPUT);
        }
        Student student = studentRepository.findByRandCode(p.getStudentRandomCode()) ;
        if(student == null){
            throw new CustomException(NOT_EXISTENCE_STUDENT) ;
        }
        return 1 ;
    }
    @Override // 아이디, 이메일 중복조회
    public String checkEmailOrUid(CheckEmailOrUidReq req) {
        if(req.getEmail() != null && req.getUid() != null){
            return "아이디 및 이메일을 하나만 입력해주세요" ;
        }
        CheckEmailOrUidRes res = mapper.checkEmailOrUid(req) ;
        if(res == null){
            return "사용할 수 있는 값입니다." ;
        }
        if(res.getUid() != null){
            return DUPLICATE_DATA_ERROR ;
        } else if (res.getEmail() != null) {
            return DUPLICATE_DATA_ERROR ;
        }
        return "사용할 수 있는 값입니다." ;
    }
    @Override // 회원정보 조회
    public ParentsUserEntity getParentsUser(String token) {
        Authentication auth = jwtTokenProvider.getAuthentication(token) ;
        SecurityContextHolder.getContext().setAuthentication(auth) ;
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        long parentsId = userDetails.getMyUser().getUserId() ;
        GetParentsUserReq req = new GetParentsUserReq();
        req.setSignedUserId(parentsId);
        return mapper.getParentsUser(req);
    }
    @Override @Transactional // 회원정보 수정
    public int patchParentsUser(PatchParentsUserReq p) {
        p.setParentsId(authenticationFacade.getLoginUserId());
        p.setAddrs(p.getZoneCode(), p.getAddr(), p.getDetail());
        return mapper.patchParentsUser(p);
    }
    @Override // 아이디 찾기
    public GetFindIdRes getFindId(GetFindIdReq req) {
        GetFindIdRes result = mapper.getFindId(req);
        if (result == null) {
            throw new CustomException(NOT_EXISTENCE_REQUEST);
        }
        return result;
    }
    @Override @Transactional // 비밀번호 수정
    public int patchPassword(PatchPasswordReq p) {
        p.setParentsId(authenticationFacade.getLoginUserId()) ;
        log.info("p: {}", p);

        log.info("parentId: {}", p.getParentsId());
        List<ParentsUserEntity> entity = mapper.selPasswordBeforeLogin(p.getUid()) ;
        if(Objects.isNull(entity)){
            throw new CustomException(NOT_EXISTENCE_PARENT) ;
        }
        if(!passwordEncoder.matches(p.getUpw(), entity.get(0).getUpw())){
            throw new CustomException(PASSWORD_NO_MATCH_ERROR) ;
        }

        String password = passwordEncoder.encode(p.getNewUpw()) ;
        p.setParentsId(entity.get(0).getParentsId()) ;
        p.setNewUpw(password) ;
        return mapper.patchPassword(p);
    }
    @Override // 로그인
    public SignInPostRes signInPost(SignInPostReq p, HttpServletResponse res) {
        log.info("providerType: {}", p.getProviderType());
        log.info("p: {}", p);
        if(p.getProviderType() == null){
            p.setProviderType(SignInProviderType.LOCAL);
        }

        Parents parentsUser = repository.findParentByUid(p.getUid()) ;
        if(parentsUser == null && p.getProviderType().equals(SignInProviderType.LOCAL)){
            throw new CustomException(NOT_EXISTENCE_PARENT) ;
        }
        log.info("parentsUser: {}", parentsUser);
        String role = "ROLE_PARENTS";
        ParentOAuth2 parentOAuth2 = new ParentOAuth2() ;
        parentOAuth2.setParentsId(parentsUser) ;
        parentOAuth2.setProviderType(SignInProviderType.LOCAL) ;
        log.info("parentOAuth2: {}", parentOAuth2);

        long parent = repository.getParentsByParentsId(parentsUser.getParentsId()) ;
        log.info("parentsId: {}", parent);

        Parents parents = repository.getParentsByProviderTypeAndParentsId(parentOAuth2.getProviderType(), parent) ;
        log.info("parents: {}", parents);

        if(!BCrypt.checkpw(p.getUpw(), parentsUser.getUpw())){
            throw new CustomException(CHECK_ID_AND_PASSWORD) ;
        }

        if(parentsUser.getAccept() != 1){
            throw new CustomException(YET_OK_USER) ;
        }

        MyUser myUser = MyUser.builder().
                userId(parentsUser.getParentsId()).
                role(role.trim()).
                build();

        List<StudentRes> student = mapper.studentList(parents.getParentsId()) ;
        log.info("student: {}", student);

//        for (StudentRes students : student) {
//            Hibernate.initialize(students.getParent());
//        }

        String accessToken = jwtTokenProvider.generateAccessToken(myUser);
        String refreshToken = jwtTokenProvider.generateRefreshToken(myUser);

        int refreshTokenMaxAge = appProperties.getJwt().getRefreshTokenCookieMaxAge();
        cookieUtils.deleteCookie(res, "refresh-token");
        cookieUtils.setCookie(res,"refresh-token", refreshToken, refreshTokenMaxAge);

        return SignInPostRes.builder().
                parentsId(parentsUser.getParentsId()).
                nm(parentsUser.getName()).
                accessToken(accessToken).
                studentList(student).
                build();
    }
    @Override // access token
    public Map<String, Object> getAccessToken(HttpServletRequest req) {
        log.info("req: {}", req);
        System.out.println("Checking for refresh token cookie");

        if (req == null) {
            throw new CustomException(NULL_HTTP_SERVLET_REQUEST);
        }

        // 요청 헤더 출력
        Enumeration<String> headerNames = req.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String headerValue = req.getHeader(headerName);
                System.out.println("Header: " + headerName + " = " + headerValue);
            }
        } else {
            System.out.println("No headers found in the request");
        }

        // 요청에서 모든 쿠키 출력
        if (req.getCookies() != null) {
            for (Cookie cookie : req.getCookies()) {
                System.out.println("Cookie Name: " + cookie.getName() + ", Cookie Value: " + cookie.getValue());
            }
        } else {
            System.out.println("No cookies found in the request");
        }

        Cookie cookie = cookieUtils.getCookie(req, "refresh-token");
        if (cookie == null) {
            System.out.println("Refresh token cookie is missing");
            throw new CustomException(NOT_EQUAL_REFRESH_TOKEN);
        }

        String refreshToken = cookie.getValue();
        System.out.println("Refresh token found: " + refreshToken);
        if (!jwtTokenProvider.isValidateToken(refreshToken)) {
            throw new CustomException(NOT_INVALID_REFRESH_TOKEN) ;
        }

        UserDetails auth = jwtTokenProvider.getUserDetailsFromToken(refreshToken);
        if (auth == null) {
            throw new CustomException(FAIL_USER_FROM_TOKEN);
        }

        MyUser myUser = ((MyUserDetails) auth).getMyUser();
        if (myUser == null) {
            throw new CustomException(NOT_EXISTENCE_USER);
        }

        String accessToken = jwtTokenProvider.generateAccessToken(myUser);

        Map<String, Object> map = new HashMap<>();
        map.put("accessToken", accessToken);
        return map;
    }
    @Override // 문자발송 비밀번호 찾기
    public void getFindPassword(GetFindPasswordReq req, Map map) {
        // 랜덤코드 6자리 생성
        String code = SmsSender.makeRandomCode();
        List<ParentsUserEntity> list = mapper.getParentUserList(req);
        // 회원정보 확인
        if(list == null || list.isEmpty()){
            throw new CustomException(NOT_EXISTENCE_USER);
        }
        try{
            // 문자보내기
            smsService.sendPasswordSms(req.getPhone(), coolsmsApiCaller, code);
        } catch (Exception e){
            throw new RuntimeException(SMS_SEND_ERROR);
        }

        map.put("RANDOM_CODE", code) ;
    }
    @Override @Transactional // 전자서명
    public SignatureRes signature(MultipartFile pic, SignatureReq req){
        if (pic == null || pic.isEmpty()) {
            throw new CustomException(NOT_EXISTENCE_SIGNATURE_FILE);
        }

        GetSignatureReq req1 = new GetSignatureReq();
        req1.setSemester(req.getSemester());
        req1.setYear(req.getYear());
        req1.setStudentPk((req.getStudentPk()));
        req1.setExamSign(req.getExamSign());

        GetSignatureRes res = mapper.getSignature(req1);
        Student student = studentRepository.getReferenceById(req.getStudentPk()) ;
        ScoreSign scoreSign = scoreSignRepository.getAllByStudentPkAndExamSignAndSemesterAndYear(student, req.getExamSign(), req.getSemester(), req.getYear()) ;
        if (res != null) {
            scoreSignRepository.delete(scoreSign) ;
            String path = String.format("sign/%d", req.getSignId());
            String delAbsoluteFolderPath = String.format("%s%s", customFileUtils.uploadPath, path);
            customFileUtils.deleteFolder(delAbsoluteFolderPath);
        }
        try {
            int result = mapper.signature(req);
            if (result != 1) {
                throw new RuntimeException("서명 등록 오류가 발생했습니다: 저장에 실패했습니다.");
            }
            // 파일 저장 경로 설정 및 폴더 생성
            String path = String.format("sign/%d", req.getSignId());
            log.info("path: {}", path);
            customFileUtils.makeFolders(path);

            // 랜덤 파일 이름 생성 및 파일 저장
            String saveFileName = customFileUtils.makeRandomFileName(pic);
            String target = String.format("%s/%s", path, saveFileName);
            log.info("target: {}", uploadPath);
            log.info("File saved at: {}", target); // target은 파일의 전체 경로를 포함한 문자열
            log.info("Trying to load file from: {}", customFileUtils.uploadPath);

            // 파일 이름 설정 및 로그 출력
            req.setPic(saveFileName);
            log.info("Saving file: {}", saveFileName); // 로그 추가
            log.info("Request object pic field: {}", req.getPic()); // 로그 추가

            // URL 생성
            String fileUrl = String.format("http://localhost:8080/pic/%s", target);
            log.info("File URL: {}", fileUrl);

            // 파일 저장
            customFileUtils.transferTo(pic, target);
        } catch (Exception e) {
            log.error("File upload error", e);
            throw new CustomException(ERROR_SIGNATURE);
        }
        log.info("req.getPic file: {}", req.getPic());
        String picName = req.getPic() ;
        int affectedRow = mapper.postSignaturePic(picName, req.getSignId()) ;
        return SignatureRes.builder()
                .signId(req.getSignId())
                .pics(picName)
                .build();
    }
    // sign pk 값으로 사진 가져오기
    public GetSignaturePicRes getSignaturePics(GetSignaturePicReq req){
        ScoreSign scoreSign = scoreSignRepository.findAllBySignId(req.getSignPk()) ;

        GetSignaturePicRes res = new GetSignaturePicRes() ;
        res.setSignId(scoreSign.getSignId()) ;
        res.setYear(scoreSign.getYear()) ;
        res.setPic(scoreSign.getPic()) ;
        res.setStuId(scoreSign.getStudentPk().getStuId()) ;
        res.setSemester(scoreSign.getSemester()) ;
        res.setExamSign(scoreSign.getExamSign()) ;

        return res ;
    }
    // sign pk 값으로 조회
    public String signatureNm(Long signPk){
        ScoreSign scoreSign = scoreSignRepository.getReferenceById(signPk) ;
        return scoreSign.getPic() ;
    }
    // 사인 조회 및 중복 삭제
    public void getSignature(GetSignatureReq req){
        GetSignatureRes res = mapper.getSignature(req) ;
        if (res != null){
            mapper.delSignature(req) ;
        }
    }
    @Override // 자녀 조회
    public List<GetStudentParentsRes> getStudentParents(String token){
        Authentication auth = jwtTokenProvider.getAuthentication(token) ;
        SecurityContextHolder.getContext().setAuthentication(auth) ;
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        long parentsId = userDetails.getMyUser().getUserId() ;
        GetParentsUserReq req = new GetParentsUserReq();
        req.setSignedUserId(parentsId);
        List<GetStudentParentsRes> list = mapper.getStudentParents(req.getSignedUserId());
        for(GetStudentParentsRes res : list){
            res.setClassId(Parser.classParser(res.getClassId()));
        }
        return list ;
    }
    // 로그인 후 소셜 연동
    public ParentOAuth2 signUpSocialLogin(SocialLoginReq req, String token){
        Authentication auth = jwtTokenProvider.getAuthentication(token) ;
        SecurityContextHolder.getContext().setAuthentication(auth) ;
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        long parentsId = userDetails.getMyUser().getUserId() ;

        Parents parents = new Parents() ;
        parents.setParentsId(parentsId) ;

        ParentOAuth2 oAuth2 = new ParentOAuth2() ;
        oAuth2.setId(req.getSocialId()) ;
        oAuth2.setName(req.getSocialName()) ;
        oAuth2.setProviderType(req.getProviderType()) ;
        oAuth2.setEmail(req.getSocialEmail()) ;
        oAuth2.setParentsId(parents) ;

        return oAuth2Repository.save(oAuth2) ;
    }
    // 소셜 로그인
    public SignInPostRes socialSignIn(SocialSignInReq req, HttpServletResponse res){
        ParentOAuth2 parentsOAuth2 = oAuth2Repository.getParentsByProviderTypeAndId(req.getProviderType(), req.getId()) ;
        if( parentsOAuth2 == null ){
            return SignInPostRes.builder()
                    .parentsId(-1)
                    .build() ;
        }

        Parents parents = repository.getParentsByProviderTypeAndUidAndParentsPk(parentsOAuth2.getProviderType(), req.getId(), parentsOAuth2.getParentsId().getParentsId()) ;
        if(parents.getAccept() != 1 ){
            throw new CustomException(YET_OK_USER) ;
        }
        log.info("parent: {}", parents) ;
        if (!Objects.equals(parents.getAuth(), "ROLE_PARENTS")) {
            throw new CustomException(NOT_ACCESS_AUTHORITY) ;
        }

        MyUser myUser = MyUser.builder()
                .userId(parents.getParentsId())
                .role(parents.getAuth())
                .build() ;
        String accessToken = jwtTokenProvider.generateAccessToken(myUser) ;
        String refreshToken = jwtTokenProvider.generateRefreshToken(myUser) ;

        int refreshTokenMaxAge = appProperties.getJwt().getRefreshTokenCookieMaxAge() ;
        cookieUtils.deleteCookie(res, appProperties.getJwt().getRefreshTokenCookieName()) ;
        cookieUtils.setCookie(res, appProperties.getJwt().getRefreshTokenCookieName(), refreshToken, refreshTokenMaxAge) ;

        List<ParentsUser> list = mapper.getParentUser(parents.getUid()) ;
        List<StudentRes> student = mapper.studentList(parents.getParentsId()) ;
        log.info("list: {}", list.get(0));

        if(list.get(0).getConnect().equals("기타") || list.get(0).getPhone().equals("010-0000-0000")){
            return SignInPostRes.builder()
                    .parentsId(parents.getParentsId())
                    .nm(parents.getName())
                    .result(2)
                    .accessToken(accessToken)
                    .studentList(student)
                    .build() ;
        }


        return SignInPostRes.builder()
                .parentsId(parents.getParentsId())
                .nm(parents.getName())
                .result(1)
                .accessToken(accessToken)
                .studentList(student)
                .build() ;
    }
    // 소셜 회원가입 시 학생 랜덤코드로 회원가입 여부 확인
    public int getStudentRandCode(RandCodeReq randCode){
        Student student = studentRepository.findByRandCode(randCode.getRandCode()) ;
        if (student.getParent() != null){
            throw new CustomException(EXISTENCE_PARENT) ;
        }
        return 1 ;
    }

    // 소셜 회원가입 시 전화번호 입력
    public ChangeNumberAndConnectRes postSocialPhoneNumber(ChangeNumberAndConnect req){
        Parents parent = repository.findParentByUid(req.getId()) ;
        parent.setPhone(req.getPhoneNumber()) ;
        parent.setConnect(req.getConnect()) ;
        repository.save(parent) ;
        ChangeNumberAndConnectRes res = new ChangeNumberAndConnectRes() ;
        res.setPhoneNumber(parent.getPhone()) ;
        res.setConnect(parent.getConnect()) ;
        res.setParentsId(parent.getParentsId()) ;
        return res ;
    }

    // 소셜 회원가입 처음부터 하기
    @Transactional
    public int socialSignUpLogin(SocialLoginSIgnUpReq req){
        Student student = studentRepository.findByRandCode(req.getRandomCode()) ;
        if(student == null) {
            throw new CustomException(NOT_EXISTENCE_STUDENT) ;
        }

        Parents parents = new Parents() ;
        parents.setUid(req.getId()) ;
        parents.setName(req.getName()) ;
        parents.setAuth("ROLE_PARENTS") ;
        parents.setPhone(req.getPhone()) ;
        parents.setConnect(req.getConnect()) ;
        parents.setEmail(req.getEmail()) ;
        parents.setAccept(2) ;
        repository.save(parents) ;

        UpdateStudentParentsIdReq req1 = new UpdateStudentParentsIdReq() ;
        req1.setParentPk(parents.getParentsId()); ;
        req1.setStudentPk(student.getStuId()) ;
        int updateStudent = mapper.updStudent(req1) ;
        log.info("updateStudent: {}", updateStudent) ;

        ParentOAuth2 oAuth2 = new ParentOAuth2() ;
        oAuth2.setParentsId(parents) ;
        oAuth2.setId(req.getId()) ;
        oAuth2.setEmail(req.getEmail()) ;
        oAuth2.setName(req.getName()) ;
        oAuth2.setProviderType(req.getSignInProviderType()) ;
        oAuth2Repository.save(oAuth2) ;

        student.setParent(parents) ;

        return 1 ;
    }
    // 선생이 학부모 리스트 조회
    public List getParentList(List<GetParentRes> list) {
        list = mapper.getParentsList(authenticationFacade.getLoginUserId());
        if (list.isEmpty()) {
            throw new CustomException(QUERY_RESULT_ERROR);
        }
        for (int i = 0; i < list.size(); i++) {
            list.get(i);
        }
        return list;
    }
}
