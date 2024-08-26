package com.green.fefu.teacher.service;


import com.green.fefu.chcommon.Parser;
import com.green.fefu.chcommon.SmsSender;
import com.green.fefu.common.AppProperties;
import com.green.fefu.common.CookieUtils;
import com.green.fefu.entity.Teacher;
import com.green.fefu.exception.CustomException;
import com.green.fefu.security.AuthenticationFacade;
import com.green.fefu.security.MyUser;
import com.green.fefu.security.jwt.JwtTokenProviderV2;
import com.green.fefu.sms.SmsService;
import com.green.fefu.teacher.model.req.*;
import com.green.fefu.teacher.repository.TeacherRepository;
import com.green.fefu.teacher.test.TeacherService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.green.fefu.exception.bch.BchErrorCode.*;
import static com.green.fefu.teacher.model.dataset.TeacherMapNamingData.*;
import static java.time.LocalDateTime.now;


import java.sql.Date;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    private final TeacherMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationFacade authenticationFacade;
    private final JwtTokenProviderV2 jwtTokenProvider;
    private final AppProperties appProperties;
    private final CookieUtils cookieUtils;
    private final SmsService smsService;
    @Value("${coolsms.api.caller}")
    private String coolsmsApiCaller;

    //    JPA
    private final TeacherRepository teacherRepository;

//=====================================================================================================================

    //    회원가입
    @Transactional
    @Override
    public Map CreateTeacher(CreateTeacherReq p, Map map){
//         벨리데이션 체크
//        1. 데이터 널체크
//        createTeacherNullCheck(p);
//        2. 데이터 타입 체크
//        createTeacherTypeCheck(p);
//        3. 비밀번호 암호화
        String hashpw = passwordEncoder.encode(p.getPassword());
        p.setPassword(hashpw);
//        4. 주소 데이터 합성
        if ((p.getAddr() == null
                && p.getZoneCode() != null)
                || (p.getAddr() != null
                && p.getZoneCode() == null)
        ) {
            throw new CustomException(ADDR_DATA_ERROR);
        } else if (p.getZoneCode() != null
                && p.getAddr() != null) {
            String fullAddr = Parser.addressParserMerge(p.getZoneCode(), p.getAddr(), p.getDetail());
            p.setFullAddr(fullAddr);
        }
//        만들어야 함
//        5. 데이터 길이 체크
//        createTeacherLengthCheck(p);

        Teacher teacher = teacherRepository.findByUid(p.getTeacherId());


//        TeacherEntity teacher = mapper.GetTeacher(
//                EntityArgument.builder()
//                        .id(p.getTeacherId())
//                        .build()
//        );

        if (teacher != null) {
            throw new CustomException(DUPLICATE_DATA_ERROR);
        }


//        5. 쿼리 실행
//        int result = mapper.CreateTeacher(p);
        teacher = new Teacher();
        teacher.setUid(p.getTeacherId());
        teacher.setUpw(p.getPassword());
        teacher.setName(p.getName());
        teacher.setPhone(p.getPhone());
        teacher.setEmail(p.getEmail());
        teacher.setGender(p.getGender());
        if (p.getBirth() != null) {
            Date date = Date.valueOf(p.getBirth());
            teacher.setBirth(date);
        }
        teacher.setAddr(p.getFullAddr());
        teacher.setAuth("ROLE_TEACHER");
//        teacher.setAccept(2);
//        teacher.setState(1);
        teacherRepository.save(teacher);
//        6. 쿼리 결과 체크
        if (teacher.getTeaId() == 0) {
            throw new CustomException(QUERY_RESULT_ERROR);
        }
//        정상 결과 시
        map.put(TEACHER_PK, teacher.getTeaId());
        return map;
    }
    /*
    private void createTeacherNullCheck(CreateTeacherReq p) throws Exception {
        validation.nullCheck(p.getTeacherId());
        validation.nullCheck(p.getPassword());
        validation.nullCheck(p.getName());
        validation.nullCheck(p.getEmail());
        validation.nullCheck(p.getPhone());
        validation.nullCheck(p.getGender());
    }

    private void createTeacherTypeCheck(CreateTeacherReq p) throws Exception {
        if (p.getBirth() != null && !p.getBirth().trim().isEmpty()) {
            validation.typeCheck(p.getBirth(), LocalDate.class, BIRTH_TYPE_ERROR);
        }
        patternCheck.idCheck(p.getTeacherId());
        patternCheck.emailCheck(p.getEmail());
        patternCheck.nameCheck(p.getName());
        patternCheck.passwordCheck(p.getPassword());
        patternCheck.phoneCheck(p.getPhone());
    }

    private void createTeacherLengthCheck(CreateTeacherReq p) throws Exception {
        validation.lengthCheck(p.getTeacherId(), TEACHER_ID_MAX_LENGTH);
        validation.lengthCheck(p.getPassword(), TEACHER_PASSWORD_MAX_LENGTH);
        validation.lengthCheck(p.getName(), TEACHER_NAME_MAX_LENGTH);
        validation.lengthCheck(p.getEmail(), TEACHER_EMAIL_MAX_LENGTH);
        validation.lengthCheck(p.getPhone(), TEACHER_PHONE_MAX_LENGTH);
        validation.lengthCheck(p.getGender(), TEACHER_GENDER_MAX_LENGTH);
        if (p.getFullAddr() != null) {
            validation.lengthCheck(p.getFullAddr(), TEACHER_ADDRESS_MAX_LENGTH);
        }
    }
    */

//=====================================================================================================================

    //    로그인
    @Override
    public Map LogInTeacher(LogInTeacherReq p, Map map, HttpServletResponse res){
//        데이터 널 체크
//        logInTeacherNullCheck(p);
//        데이터 타입 체크
//        logInTeacherTypeCheck(p);
//        유저 select

        Teacher teacher = teacherRepository.findByUid(p.getTeacherId());

//        TeacherEntity teacher = mapper.GetTeacher(
//                EntityArgument.builder()
//                        .id(p.getTeacherId())
//                        .build()
//        );

//        아이디로 확인 안됐을때
        if (teacher == null) {
            throw new CustomException(ID_NOT_FOUND_ERROR);
        }
        if(teacher.getAccept() == 2){
            throw new CustomException(NOT_YET_ACCEPT);
        }
//        비밀번호 매치 체크
//        암호화된 비밀번호가 다를때
        else if (!passwordEncoder.matches(p.getPassword(), teacher.getUpw())) {
            throw new CustomException(PASSWORD_NO_MATCH_ERROR);
        }

//        JWT 토큰 발급
        String accessToken = createToken(teacher, res);

//        담당 학급 받아오기
        String teacherClass = mapper.getCurrentClassesByTeacherId(teacher.getTeaId());
        String[] tClass = null;
//        담당 학급이 있을 때 ( 방금 회원가입하면 담당학급이 없음 )
        if (teacherClass != null) {
            tClass = Parser.classParserArray(teacherClass);
            map.put(TEACHER_GRADE, tClass[0]);
            map.put(TEACHER_CLASS, tClass[1]);
        }

//        데이터 삽입
        map.put(TEACHER_NAME, teacher.getName());
        map.put(TEACHER_EMAIL, teacher.getEmail());
        map.put(TEACHER_ACCESS_TOKEN, accessToken);
        return map;
    }

    /*
    private void logInTeacherNullCheck(LogInTeacherReq p) throws Exception {
        validation.nullCheck(p.getTeacherId());
        validation.nullCheck(p.getPassword());
    }

    private void logInTeacherTypeCheck(LogInTeacherReq p) throws Exception {
        patternCheck.idCheck(p.getTeacherId());
        patternCheck.passwordCheck(p.getPassword());
    }
    */
    private String createToken(Teacher teacher, HttpServletResponse res){
        MyUser myUser = MyUser.builder()
                .userId(teacher.getTeaId())
                .role(teacher.getAuth().trim())
                .build();
        String accessToken = jwtTokenProvider.generateAccessToken(myUser);
        String refreshToken = jwtTokenProvider.generateRefreshToken(myUser);

//        refreshToken은 보안 쿠키를 이용해서 처리
        int refreshTokenMaxAge = appProperties.getJwt().getRefreshTokenCookieMaxAge();
        cookieUtils.deleteCookie(res, "refresh-token");
        cookieUtils.setCookie(res, "refresh-token", refreshToken, refreshTokenMaxAge);
        return accessToken;
    }


//=====================================================================================================================

    //    아이디, 이메일 중복 확인
    @Override
    public void CheckDuplicate(CheckDuplicateReq p){

//        dataCheck(p);

//        유저 select (아이디, email)
//        TeacherEntity teacher = mapper.GetTeacher(
//                EntityArgument.builder()
//                        .id(p.getId())
//                        .email(p.getEmail())
//                        .build()
//        );
        Teacher teacher;
        if (p.getId() != null) {
            teacher = teacherRepository.findByUid(p.getId());
            if(teacher != null) {
                throw new CustomException(DUPLICATE_DATA_ERROR);
            }
        } else if (p.getEmail() != null) {
            teacher = teacherRepository.findByEmail(p.getEmail());
            if(teacher != null) {
                throw new CustomException(DUPLICATE_DATA_ERROR);
            }
        } else {
            throw new CustomException(MULTIPLE_DATA_ERROR);
        }
    }

    /*
    private void dataCheck(CheckDuplicateReq p) {
        if (p.getId() != null && p.getEmail() != null) {
            throw new CustomException(MULTIPLE_DATA_ERROR);
        }

//        아이디 형식 확인
        if (p.getId() != null) {
            patternCheck.idCheck(p.getId());
        }
//        이메일 형식 확인
        else if (p.getEmail() != null) {
            patternCheck.emailCheck(p.getEmail());
        } else {
            throw new CustomException(ESSENTIAL_DATA_NOT_FOUND_ERROR);
        }
    }
     */

//=====================================================================================================================

    //    선생님 아이디 찾기
    @Override
    public Map FindTeacherId(FindTeacherIdReq p, Map map){
//        널 체크
//        FindTeacherIdNullCheck(p);
//        타입 체크
//        FindTeacherIdTypeCheck(p);

//        TeacherEntity teacher = mapper.GetTeacher(
//                EntityArgument.builder()
//                        .name(p.getName())
//                        .phone(p.getPhone())
//                        .build()
//        );
        Teacher teacher = teacherRepository.findByNameAndPhone(p.getName(), p.getPhone());
        if (teacher == null) {
            throw new CustomException(NOT_FOUND_USER_ERROR);
        }
        map.put(TEACHER_UID, teacher.getTeaId());
        return map;
    }
/*
    private void FindTeacherIdNullCheck(FindTeacherIdReq p) throws Exception {
        validation.nullCheck(p.getName());
        validation.nullCheck(p.getPhone());
    }

    private void FindTeacherIdTypeCheck(FindTeacherIdReq p) throws Exception {
        patternCheck.nameCheck(p.getName());
        patternCheck.phoneCheck(p.getPhone());
    }

 */
//=====================================================================================================================

    //    선생님 비밀번호 찾기
    @Override
    public void FindTeacherPassword(FindTeacherPasswordReq p, Map map){
//        널 체크
//        FindTeacherPasswordNullCheck(p);
//        타입 체크
//        FindTeacherPasswordTypeCheck(p);
//        TeacherEntity teacher = mapper.GetTeacher(
//                EntityArgument.builder()
//                        .id(p.getId())
//                        .phone(p.getPhone())
//                        .build()
//        );
        Teacher teacher = teacherRepository.findByUid(p.getId());
        if (teacher == null) {
            throw new CustomException(NOT_FOUND_USER_ERROR);
        }

//        랜덤 코드 6자리 생성
        String code = SmsSender.makeRandomCode();
//        휴대폰으로 문자 보내기
//        짜야함 ( 미완성 )
        try {
            smsService.sendPasswordSms(p.getPhone(), coolsmsApiCaller, code);
        } catch (Exception e) {
            throw new CustomException(SMS_SEND_ERROR);
        }


        map.put("RANDOM_CODE", code);
    }
/*
    private void FindTeacherPasswordNullCheck(FindTeacherPasswordReq p) throws Exception {
        validation.nullCheck(p.getId());
        validation.nullCheck(p.getPhone());
    }

    private void FindTeacherPasswordTypeCheck(FindTeacherPasswordReq p) throws Exception {
        patternCheck.idCheck(p.getId());
        patternCheck.phoneCheck(p.getPhone());
    }


 */
//=====================================================================================================================

    //    선생님 비밀번호 변경 ( 로그인 전 )
    @Transactional
    @Override
    public Teacher ChangePassWord(ChangePassWordReq p){

//        널체크
//        ChangePassWordNullCheck(p);

//        로그인 햇을때는 TeacherId 값이 null 일 수 있기 때문이다.
//        validation.nullCheck(p.getTeacherId());

//        유저 select
//        TeacherEntity teacher = getTEntity(p);
        long teacherPk = authenticationFacade.getLoginUserId() ;
        Teacher teacher = teacherRepository.getReferenceById(teacherPk) ;
//        Teacher teacher = teacherRepository.findByUid(p.getTeacherUid());

//        타입 체크
//        ChangePassWordTypeCheck(p);
        if(!passwordEncoder.matches(p.getOldPassWord(), teacher.getUpw())){
            throw new CustomException(PASSWORD_NO_MATCH_ERROR) ;
        }

//        비밀번호 암호화
        String hashpw = passwordEncoder.encode(p.getPassWord());
//        p.setPassWord(hashpw);

        teacher.setUpw(hashpw);
        teacherRepository.save(teacher);

        return teacher ;
//        쿼리 실행
    }
/*
    private void ChangePassWordNullCheck(ChangePassWordReq p) throws Exception {
        validation.nullCheck(p.getPassWord());
    }

    private TeacherEntity getTEntity(ChangePassWordReq p) throws Exception {
        TeacherEntity teacher;
        log.info("entity : {}", p);
        validation.nullCheck(p.getTeacherId());
        teacher = mapper.GetTeacher(
                EntityArgument.builder()
                        .id(p.getTeacherId())
                        .build()
        );

        log.info("asdasd");
        if (teacher == null) {
            throw new CustomException(ID_NOT_FOUND_ERROR);
        }
        log.info("qqqqq");
        return teacher;
    }

    private void ChangePassWordTypeCheck(ChangePassWordReq p) throws Exception {
        patternCheck.passwordCheck(p.getPassWord());
    }


 */
//=====================================================================================================================

    //    선생님 내정보 불러오기
    @Override
    public Map TeacherDetail(Map map){
//        TeacherEntity teacher = mapper.GetTeacher(
//                EntityArgument.builder()
//                        .pk(authenticationFacade.getLoginUserId())
//                        .build()
//        );
        Teacher teacher = teacherRepository.getReferenceById(authenticationFacade.getLoginUserId());

        String teacherClass = mapper.getCurrentClassesByTeacherId(teacher.getTeaId());
        String[] tClass = Parser.classParserArray(teacherClass);

        map.put(TEACHER_UID, teacher.getUid());
        map.put(TEACHER_NAME, teacher.getName());
        map.put(TEACHER_PHONE, teacher.getPhone());
        map.put(TEACHER_EMAIL, teacher.getEmail());
        map.put(TEACHER_GENDER, teacher.getGender());
        map.put(TEACHER_BIRTH, teacher.getBirth());
        map.put(TEACHER_GRADE, tClass[0]);
        map.put(TEACHER_CLASS, tClass[1]);


//        주소 자를껀지 물어보고 잘라야 하면 잘라서 보내주기
//        (현재 우편번호 # 주소 ) 임
        map.put(TEACHER_ADDR, teacher.getAddr());

        return map;
    }

//=====================================================================================================================

    //    선생님 정보 변경
    @Transactional
    @Override
    public void ChangeTeacher(ChangeTeacherReq p){
//        p.setPk(authenticationFacade.getLoginUserId());

//        타입 체크 and 데이터 길이 체크
//        ChangeTeacherErrorCheck(p);

//        주소값 합성
        p.setFullAddr(Parser.addressParserMerge(p.getZoneCode(), p.getAddr(), p.getDetail()));

//        쿼리 실행
//        int result = mapper.ChangeTeacher(p);
        Teacher teacher = teacherRepository.getReferenceById(authenticationFacade.getLoginUserId());
        if(!Objects.equals(p.getPhone(), teacher.getPhone())){
            teacher.setPhone(p.getPhone());
        }
        if(!Objects.equals(p.getName(), teacher.getName())){
            teacher.setName(p.getName());
        }
        if(!Objects.equals(p.getEmail(), teacher.getEmail())){
            teacher.setEmail(p.getEmail());
        }
        if(!Objects.equals(p.getFullAddr(), teacher.getAddr())){
            teacher.setAddr(p.getFullAddr());
        }
        teacherRepository.save(teacher);
//        if (result != 1) {
//            throw new CustomException(QUERY_RESULT_ERROR);
//        }
    }
/*
    private void ChangeTeacherErrorCheck(ChangeTeacherReq p) throws Exception {
        if (p.getName() != null) {
            patternCheck.nameCheck(p.getName());
            validation.lengthCheck(p.getName(), TEACHER_NAME_MAX_LENGTH);
        }
        if (p.getPhone() != null) {
            patternCheck.phoneCheck(p.getPhone());
            validation.lengthCheck(p.getPhone(), TEACHER_PHONE_MAX_LENGTH);
        }
        if (p.getEmail() != null) {
            patternCheck.emailCheck(p.getEmail());
            validation.lengthCheck(p.getEmail(), TEACHER_EMAIL_MAX_LENGTH);
        }
    }

 */
}
