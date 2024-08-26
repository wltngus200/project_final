package com.green.fefu.student.service;

import com.green.fefu.chcommon.Parser;
import com.green.fefu.chcommon.PatternCheck;
import com.green.fefu.chcommon.Validation;
import com.green.fefu.common.AppProperties;
import com.green.fefu.common.CookieUtils;
import com.green.fefu.common.CustomFileUtils;
import com.green.fefu.entity.Parents;
import com.green.fefu.entity.Student;
import com.green.fefu.entity.Teacher;
import com.green.fefu.exception.CustomException;
import com.green.fefu.parents.model.ParentsUser;
import com.green.fefu.parents.repository.ParentRepository;
import com.green.fefu.security.AuthenticationFacade;
import com.green.fefu.security.MyUser;
import com.green.fefu.security.jwt.JwtTokenProviderV2;
import com.green.fefu.student.model.dto.*;
import com.green.fefu.student.model.req.*;
import com.green.fefu.student.repository.StudentRepository;
import com.green.fefu.student.test.StudentService;
import com.green.fefu.teacher.repository.TeacherRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.green.fefu.exception.bch.BchErrorCode.*;
import static com.green.fefu.student.model.dataset.StudentMapNamingData.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentMapper mapper;
    //    private final Validation validation;
//    private final PatternCheck patternCheck;
    private final CustomFileUtils customFileUtils;
    private final AuthenticationFacade authenticationFacade;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final ParentRepository parentRepository;
    private final JwtTokenProviderV2 jwtTokenProvider;
    private final AppProperties appProperties;
    private final CookieUtils cookieUtils;



    //    학생 데이터 넣기
    @Transactional
    @Override
    public Map createStudent(createStudentReq p, MultipartFile pic, Map map) {
//        널 체크
//        createStudentNullCheck(p);
//        타입 체크
//        createStudentTypeCheck(p);
//        길이 체크
//        createStudentLengthCheck(p);

        p.setPic(fileNameChange(pic));
        Student a = studentRepository.findStudentByGrade(Integer.parseInt(p.getGrade()));
        if (a != null) {
            throw new CustomException(GRADE_DUPLICATE_ERROR);
        }
        a = studentRepository.findStudentByUid(p.getStudentId());
        if (a != null) {
            throw new CustomException(STUDENT_ID_DUPLICATE_ERROR);
        }
//        int result = mapper.createStudent(p);
        Student student = new Student();
        student.setPic(p.getPic());
        student.setName(p.getName());
        student.setGender(p.getGender());
        student.setGrade(Integer.parseInt(p.getGrade()));
        Date date = Date.valueOf(p.getBirth());
        student.setBirth(date);
        student.setAddr(p.getAddr());
        student.setEtc(p.getEtc());
        student.setEngName(p.getEngName());
        student.setPhone(p.getPhone());
        student.setUid(p.getStudentId());
        student.setEmail(p.getEmail());
        student.setAuth("ROLE_STUDENT");
        String hashpw = passwordEncoder.encode(p.getStudentPw());
        p.setStudentPw(hashpw);
        student.setUpw(p.getStudentPw());
        studentRepository.save(student);
//        파일 관련 프로세스
        fileProcess(p, pic);

        if (student.getStuId() == null || student.getStuId() == 0) {
            throw new CustomException(QUERY_RESULT_ERROR);
        }

////        grade에서 반 정보 뽑아내기 ( 지금은 10101 << 이 폼임 )
//        String classData = p.getGrade().substring(0, 3);
//        ClassInsert classInsert = new ClassInsert();
//        classInsert.setClassPk(Integer.parseInt(classData));
//        classInsert.setTeacherPk(p.getTeacherPk());
//        classInsert.setStudentPk(p.getPk());
////        반이 없으면 반 생성
//        mapper.insertClassIfNotExists(classInsert);
////        반에 학생 추가
//        mapper.insertClassStudent(classInsert);


        map.put(STUDENT_PK, student.getStuId());

        return map;
    }

    /*
        private void createStudentNullCheck(createStudentReq p){
            validation.nullCheck(p.getGrade());
            validation.nullCheck(p.getName());
            validation.nullCheck(p.getGender());
            validation.nullCheck(p.getBirth());
            validation.nullCheck(p.getAddr());
            validation.nullCheck(p.getEtc());
            validation.nullCheck(p.getEngName());
            validation.nullCheck(p.getPhone());
        }

        private void createStudentTypeCheck(createStudentReq p){
            patternCheck.nameCheck(p.getName());
            patternCheck.phoneCheck(p.getPhone());
            validation.typeCheck(p.getBirth(), LocalDate.class, BIRTH_TYPE_ERROR);
            patternCheck.gradeCheck(p.getGrade());

        }

        private void createStudentLengthCheck(createStudentReq p){
            validation.lengthCheck(p.getName(), STUDENT_MAX_NAME);
            validation.lengthCheck(p.getGender(), STUDENT_MAX_GENDER);
            if (p.getPic() != null) {
                validation.lengthCheck(p.getPic(), STUDENT_MAX_PIC);
            }
            validation.lengthCheck(p.getAddr(), STUDENT_MAX_ADDR);
            validation.lengthCheck(p.getEtc(), STUDENT_MAX_ETC);
            validation.lengthCheck(p.getPhone(), STUDENT_MAX_PHONE);
            validation.lengthCheck(p.getEngName(), STUDENT_MAX_ENG_NAME);
        }


     */
    private void fileProcess(createStudentReq p, MultipartFile pic) {
        if (pic != null) {
            try {
                String path = String.format("student/%s", p.getPk());
                customFileUtils.makeFolders(path);
                String target = String.format("%s/%s", path, p.getPic());
                customFileUtils.transferTo(pic, target);
            } catch (Exception e) {
                throw new CustomException(FILE_ERROR);
            }
        }
    }

    private String fileNameChange(MultipartFile pic) {
        if (pic != null) {
            return customFileUtils.makeRandomFileName(pic);
        }
        throw new CustomException(FILE_ERROR);
    }

    //=====================================================================================================================
//   학생 삭제
    @Override
    @Transactional
    public void deleteStudent(deleteStudentReq p) {
//        널체크
//        deleteStudentNullCheck(p);
//        타입체크
//        deleteStudentTypeCheck(p);

//        int result = mapper.deleteStudent(p);
        Student student = studentRepository.getReferenceById(p.getPk());
        studentRepository.delete(student);
//        if (student == null) {
//            throw new CustomException(QUERY_RESULT_ERROR);
//        }
    }

    /*
        private void deleteStudentNullCheck(deleteStudentReq p){
            validation.nullCheck(p.getPk().toString());
            validation.nullCheck(p.getState().toString());
        }

        private void deleteStudentTypeCheck(deleteStudentReq p){
            validation.typeCheck(p.getPk().toString(), Long.class, TYPE_ERROR);
            validation.typeCheck(p.getState().toString(), Integer.class, TYPE_ERROR);
            if (p.getState() < 1 || p.getState() > 3) {
                throw new CustomException(STATE_VARIATION_ERROR);
            }
        }
    */
    //=====================================================================================================================
//    선생의 담당 학급 학생리스트 불러오기
    @Override
    public List getStudentList(List<getStudent> list) {
        list = mapper.getStudentList(authenticationFacade.getLoginUserId());
        if (list.isEmpty()) {
            throw new CustomException(QUERY_RESULT_ERROR);
        }
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setNum(i + 1);
        }
        return list;
    }




    //=====================================================================================================================
    @Override
    public Map getStudentDetail(long pk, Map map) {
        getDetail result = mapper.getStudentDetail(pk);
        if (result == null) {
            throw new CustomException(QUERY_RESULT_ERROR);
        }
        map.put(STUDENT_PK, result.getPk());
        map.put(STUDENT_NAME, result.getName());
        map.put(STUDENT_GENDER, result.getGender());
        map.put(STUDENT_BIRTH, result.getBirth());
        map.put(STUDENT_PHONE, result.getPhone());
        map.put(STUDENT_ETC, result.getEtc());
        map.put(STUDENT_CREATED_AT, result.getCreatedAt());
        map.put(STUDENT_PIC, result.getPic());
        map.put(PARENT_ID, result.getParentId());
        map.put(PARENT_NAME, result.getParentName());
        map.put(CONNET, result.getConnet());
        map.put(PARENT_PHONE, result.getParentPhone());
        map.put(TEACHER_NAME, result.getTeacherName());
        String[] classData = null;
        if (result.getUClass() != null) {
            classData = Parser.classParserArray(result.getUClass());
            map.put(STUDENT_GRADE, classData[0]);
            map.put(STUDENT_CLASS, classData[1]);
        } else {
            throw new CustomException(GRADE_DATA_NOT_FOUND);
        }
        String[] addr;
        if (result.getAddr() != null) {
            addr = Parser.addressParser(result.getAddr());
//            log.info("data : {}", classData);
            map.put(STUDENT_ZONE_CODE, addr[0]);
            map.put(STUDENT_ADDR, addr[1]);
            map.put(STUDENT_DETAIL, addr[2]);
        }

//       역대 etc
//        이건 구현 해야함
//        리스트 젤 마지막꺼 자르고 줘야함 ( 마지막껀 현재 정보기 때문 )
        List<prevStudentEtc> prevEtc = mapper.selPrevEtc(pk);
        log.info("prevEtc.size() = " + prevEtc.size());
        prevEtc.remove(prevEtc.size() - 1);
        log.info("removePrevEtc.size() = " + prevEtc.size());
        for (int i = 0; i < prevEtc.size(); i++) {
            String etcClass = Parser.classParser(prevEtc.get(i).getUClass());
            if (etcClass == null) {
                break;
            }
            prevEtc.get(i).setUClass(etcClass);
        }
        map.put(PREV_ETC_LIST, prevEtc);

        return map;
    }

    //=====================================================================================================================
//    학생 정보 수정
    @Override
    @Transactional
    public void updateStudent(updateStudentReq p, MultipartFile pic) {
//        updateStudentDataCheck(p);
        p.setFullAddr(Parser.addressParserMerge(p.getStudentZoneCode(), p.getStudentAddr(), p.getStudentDetail()));
        log.info("updateStudentDataCheck = " + p);
//        int result = mapper.updateStudent(p);
        Student student = studentRepository.getReferenceById(p.getStudentPk());
        if (p.getStudentName() != null && !student.getName().equals(p.getStudentName())) {
            student.setName(p.getStudentName());
        }
        if (p.getStudentPhone() != null && !student.getPhone().equals(p.getStudentPhone())) {
            student.setPhone(p.getStudentPhone());
        }
        if (p.getFullAddr() != null
                && student.getAddr() != null
                && !student.getAddr().equals(p.getFullAddr())) {
            student.setAddr(p.getFullAddr());
        }
        if (p.getStudentEtc() != null
                && student.getEtc() != null
                && !student.getEtc().equals(p.getStudentEtc())) {
            student.setEtc(p.getStudentEtc());
        }
        if (p.getStudentBirth() != null) {
            Date date = Date.valueOf(p.getStudentBirth());
            if (!student.getBirth().equals(date)) {
                student.setBirth(date);
            }
        }
        if (pic != null) {
            student.setPic(fileNameChange(pic));
            try {

                String path = String.format("student/%s", student.getStuId());
                customFileUtils.deleteFolder(String.format("%s/%s", customFileUtils.uploadPath, path));
                customFileUtils.makeFolders(path);
                String target = String.format("%s/%s", path, student.getPic());
                customFileUtils.transferTo(pic, target);
            } catch (Exception e) {
                throw new CustomException(FILE_ERROR);
            }

        }
        studentRepository.save(student);
    }

    /*
        private void updateStudentDataCheck(updateStudentReq p) {
            if (p.getStudentAddr() != null && p.getStudentZoneCode() != null) {
                p.setFullAddr(Parser.addressParserMerge(p.getStudentZoneCode(), p.getStudentAddr(), p.getStudentDetail()));
            }
            if (p.getStudentPk() < 1) {
                throw new CustomException(REQUIRED_DATA_ERROR);
            }
            if (p.getStudentPhone() != null) {
                patternCheck.phoneCheck(p.getStudentPhone());
            }
            if (p.getStudentName() != null) {
                patternCheck.nameCheck(p.getStudentName());
            }
        }
    */
    //=====================================================================================================================
//    부모 없는 학생 List 출력
    @Override
    public Map getStudentListForParent(String searchWord) {
        Student student = studentRepository.findByRandCode(searchWord);
        String response = "성공적으로 등록 되었습니다.";
        Map map = new HashMap();
        if (student == null) {
            response = "학생 코드를 찾을 수 없습니다.";
        }
        else if (student.getParent() != null) {
            response = "이미 보호자가 등록된 학생입니다.";
        }
        map.put("response", response);
        return map;
    }


    @Transactional
    public void studentAdvanceGrade(List<studentAdvanceGradeReq> p) {
//        널체크
        if (p == null || p.isEmpty()) {
            throw new CustomException(ESSENTIAL_DATA_NOT_FOUND_ERROR);
        }
        for (studentAdvanceGradeReq item : p) {
//            studentAdvanceGradeNullCheck(item);
//        타입 체크
//            studentAdvanceGradeTypeCheck(item);
//        데이터 파싱
            studentAdvanceGradeParse(item);
        }

//        쿼리 실행
        for (studentAdvanceGradeReq item : p) {
            String etc = mapper.getStudentEtc(Long.parseLong(item.getStudentPk()));
            item.setEtc(etc);
            mapper.updStudentEtc(Long.parseLong(item.getStudentPk()), etc);
            int result1 = mapper.updStudentGrade(item);
            int result2 = mapper.insNewClass(item);
//        쿼리 에러
            if (result1 != 1) {
                throw new CustomException(QUERY_RESULT_ERROR);
            }
            if (result2 != 1) {
                throw new CustomException(QUERY_RESULT_ERROR);
            }
        }
    }

    /*
    private void studentAdvanceGradeNullCheck(studentAdvanceGradeReq p) {
        if (p.getStudentPk() != null && p.getGrade() != null) {
            return;
        } else {
            throw new CustomException(REQUIRED_DATA_ERROR);
        }
    }

    private void studentAdvanceGradeTypeCheck(studentAdvanceGradeReq p) {
        validation.typeCheck(p.getStudentPk(), Long.class, TYPE_ERROR);
        validation.typeCheck(p.getGrade(), Integer.class, TYPE_ERROR);
        patternCheck.gradeNumberCheck(p.getGrade());
    }
*/
    private void studentAdvanceGradeParse(studentAdvanceGradeReq p) {
//        20101 -> 201
        p.setSubNumber(p.getGrade().substring(0, 3));
    }

    public Map studentSignIn(StudentSignInReq p, HttpServletResponse res){
        Student student = studentRepository.findStudentByUid(p.getStudentUid());
        if(student == null){
            throw new CustomException(NOT_FOUND_USER_ERROR);
        }
        if(!passwordEncoder.matches(p.getStudentPwd(),student.getUpw())){
            throw new CustomException(PASSWORD_NO_MATCH_ERROR);
        }
        Map map = new HashMap();
        map.put(STUDENT_PK, student.getStuId());
        map.put(STUDENT_PIC, student.getPic());
        map.put(STUDENT_NAME, student.getName());
        String[] data = Parser.classParserArray(Integer.toString(student.getGrade()));
        map.put(STUDENT_GRADE,data[0]);
        map.put(STUDENT_CLASS, data[1]);
        map.put(STUDENT_CLASS_NUMBER,data[2]);
        String teacherName = mapper.findTeacherName(student.getStuId());
        map.put(TEACHER_NAME, teacherName);
        String accessToken = createToken(student, res);
        map.put(STUDENT_ACCESS_TOKEN, accessToken);
        return map;
    }
    private String createToken(Student student, HttpServletResponse res){
        MyUser myUser = MyUser.builder()
                .userId(student.getStuId())
                .role(student.getAuth().trim())
                .build();
        String accessToken = jwtTokenProvider.generateAccessToken(myUser);
        String refreshToken = jwtTokenProvider.generateRefreshToken(myUser);

//        refreshToken은 보안 쿠키를 이용해서 처리
        int refreshTokenMaxAge = appProperties.getJwt().getRefreshTokenCookieMaxAge();
        cookieUtils.deleteCookie(res, "refresh-token");
        cookieUtils.setCookie(res, "refresh-token", refreshToken, refreshTokenMaxAge);
        return accessToken;
    }

    public void addChild(AddChild p){
        Optional<Student> optionalStudent = studentRepository.findStudentsByRandCode(p.getRandCode());
        log.info("student : {}", optionalStudent.toString());
        if (optionalStudent.isEmpty()) {
            throw new CustomException(NOT_FOUND_USER_ERROR);
        }
        if(optionalStudent.get().getParent() != null){
            throw new CustomException(MULTIPLE_PARENT_ERROR);
        }
        Parents parents = parentRepository.getReferenceById(authenticationFacade.getLoginUserId());
        optionalStudent.get().setParent(parents);
        studentRepository.save(optionalStudent.get());
    }
}