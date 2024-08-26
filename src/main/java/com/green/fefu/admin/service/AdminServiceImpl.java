package com.green.fefu.admin.service;


import com.green.fefu.admin.model.dto.*;
import com.green.fefu.admin.model.req.*;
import com.green.fefu.admin.test.AdminService;
import com.green.fefu.chcommon.Parser;
import com.green.fefu.entity.*;
import com.green.fefu.entity.Class;
import com.green.fefu.exception.CustomException;
//import com.green.fefu.parents.ParentRepository;
import com.green.fefu.parents.repository.ParentRepository;
import com.green.fefu.student.repository.ClassRepository;
//import com.green.fefu.parents.repository.ParentRepository;
import com.green.fefu.student.repository.StudentClassRepository;
import com.green.fefu.student.repository.StudentRepository;
import com.green.fefu.teacher.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.green.fefu.admin.model.dataset.AdminMapNamingData.*;
import static com.green.fefu.exception.bch.BchErrorCode.*;


import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminMapper mapper;
    private final TeacherRepository teacherRepository;
    private final ParentRepository parentRepository;
    private final ClassRepository classRepository;
    private final int PrarentCode = 1,TeacherCode = 2,StudentCode = 3;
    private final StudentClassRepository studentClassRepository;
    private final StudentRepository studentRepository;


    //    유저 리스트 가져오기
    public Map<String,Object> findUnAcceptList(FindUnAcceptListReq p, Map<String,Object> map) {
        List<GetUserListDto> list;
        List<Map<String,Object>> result = new ArrayList<>();
//        부모 리스트 가져오기
        if (p.getP() == 1) {
            list = mapper.getParentList(p.getSearchWord());
            map.put(CODE, "학부모");
        }
//        교직원 리스트 가져오기
        else if (p.getP() == 2) {
            list = mapper.getTeacherList(p.getSearchWord());
            map.put(CODE, "교직원");
        } else {
            throw new CustomException(DIVISION_ERROR);
        }


//        해야함
//      클래스 아이디 기준으로 학년 반 나눠야함
        for (GetUserListDto getUserListDto : list) {
            Map<String,Object> dto = new HashMap<>();
            dto.put(PK, getUserListDto.getPk());
            dto.put(ID, getUserListDto.getId());
            dto.put(NAME, getUserListDto.getName());

            String grade = getUserListDto.getGrade();
            if (grade != null && !grade.isEmpty()) {
                String[] tClass = Parser.classParserArray(getUserListDto.getGrade());
                dto.put(GRADE, tClass[0]); // 학년
                dto.put(CLASS, tClass[1]); // 반
                dto.put(CREATED_AT, getUserListDto.getCreatedAt());
            } else {
                dto.put(GRADE, null); // 학년
                dto.put(CLASS, null); // 반
                dto.put(CREATED_AT, getUserListDto.getCreatedAt());
            }
            result.add(dto);
        }
        map.put(LIST, result);
        return map;
    }

    //=====================================================================================================================
//    유저 삭제
    @Transactional
    public void deleteUser(adminUserReq p) {
//        int result;
//        널체크
//        deleteUserNullCheck(p);


        //        부모 리스트 가져오기
        if (p.getP() == 1) {
//            result = mapper.delParent(p.getPk());
            Parents parent = parentRepository.getReferenceById(p.getPk());
            parentRepository.delete(parent);
        }
//        교직원 리스트 가져오기
        else if (p.getP() == 2) {
//            result = mapper.delTeacher(p.getPk());
            Teacher teacher = teacherRepository.getReferenceById(p.getPk());
            teacherRepository.delete(teacher);
        } else {
            throw new CustomException(DIVISION_ERROR);
        }
    }

//    private void deleteUserNullCheck(adminUserReq p){
//        validation.nullCheck(p.getP().toString());
//        validation.nullCheck(p.getPk().toString());
//    }

    //=====================================================================================================================
//    유저 승인
    @Transactional
    public void acceptUser(adminUserReq p) {
//        int result;
//        널체크
//        acceptUserNullCheck(p);
        //        부모 리스트 가져오기
        if (p.getP() == 1) {
//            result = mapper.updParent(p.getPk());

//            if (result != 1) {
//                throw new CustomException(QUERY_RESULT_ERROR);
//            }
            Parents parent = parentRepository.getReferenceById(p.getPk());
            parent.setAccept(1);
            parentRepository.save(parent);
        }
//        교직원 리스트 가져오기
        else if (p.getP() == 2) {
//            result = mapper.updTeacher(p.getPk());
//            if (result != 1) {
//                throw new CustomException(QUERY_RESULT_ERROR);
//            }
            Teacher teacher = teacherRepository.getReferenceById(p.getPk());
            teacher.setAccept(1);
            teacherRepository.save(teacher);
        } else {
            throw new CustomException(DIVISION_ERROR);
        }


    }

//    private void acceptUserNullCheck(adminUserReq p){
//        validation.nullCheck(p.getP().toString());
//        validation.nullCheck(p.getPk().toString());
//    }

    public List<Map<String,Object>> findUserList(FindUserListReq p, List<Map<String,Object>> list) {


//  부모
        if (p.getP() == PrarentCode) {
            List<Parents> parentList;
            if (p.getSearchWord() != null) {
                parentList = parentRepository.findByNameContainingAndStateIsAndAcceptIs(p.getSearchWord(), p.getCheck(), 1);
            } else {
                parentList = parentRepository.findAllByStateIsAndAcceptIs(p.getCheck(), 1);
            }
            if (parentList == null || parentList.isEmpty()) {
                return new ArrayList<>();
            }
            for (Parents parent : parentList) {
                Map<String,Object> map = new HashMap<>();  // 새 map 객체 생성
                map.put(STATE, parent.getState() == 1 ? "활성화" : "비활성화");
                map.put(ID, parent.getUid());
                map.put(NAME, parent.getName());
                map.put(PK, parent.getParentsId());
//                    입학 ( 부모기준 현재까지 입학했던 자녀 수 -> 자녀기준 부모 PK 매칭 및 카운트 )
                Long totalChild = studentRepository.countByParent(parent);
                map.put(TOTAL_CHILD, totalChild);
//                    재학 ( 현재 재학중인 자녀 수 ( STATE 1인 애들 )
                Long inSchoolChild = studentRepository.countByParentAndStateIs(parent, 1);
                map.put(IN_SCHOOL_CHILD, inSchoolChild);
//                    가입일
                map.put(CREATED_AT, parent.getCreatedAt());
//                    자녀 리스트
                List<Student> studentList = studentRepository.findStudentsByParentOrderByGradeAsc(parent);
                if (studentList != null && !studentList.isEmpty()) {
                    List<Map<String,Object>> result = new ArrayList<>();
                    for (Student student : studentList) {
                        Map<String,Object> data = new HashMap<>();
                        data.put(STUDENT_ID, student.getUid());
                        data.put(STUDENT_NAME, student.getName());
                        data.put(STUDENT_PK, student.getStuId());
                        data.put(STUDENT_STATE, switch (student.getState()) {
                                    case 1 -> "재학중";
                                    case 2 -> "전학";
                                    case 3 -> "졸업";
                                    case 4 -> "퇴학";
                                    default -> "정해진 값 없음";
                                });
                        data.put(STUDENT_CODE, student.getRandCode());
                        String[] gradeClass = Parser.classParserArray(Long.toString(student.getGrade()));
                        data.put(STUDENT_GRADE, gradeClass[0]);
                        data.put(STUDENT_CLASS, gradeClass[1]);
//                        신청, 학적 변동
                        result.add(data);
                    }
                    map.put(STUDENT_LIST, result);
                }
                list.add(map);
            }
        }
//  교직원
        else if (p.getP() == TeacherCode) {
            List<Teacher> teacherList;
            if (p.getSearchWord() != null) {
                teacherList = teacherRepository.findByNameContainingAndStateIsAndAcceptIs(p.getSearchWord(), p.getCheck(), 1);
            } else {
                teacherList = teacherRepository.findAllByStateIsAndAcceptIs(p.getCheck(), 1);
            }
            if (teacherList == null || teacherList.isEmpty()) {
                return new ArrayList<>();
            }
            for (Teacher teacher : teacherList) {
                Map<String,Object> map = new HashMap<>();  // 새 map 객체 생성
                Class c = classRepository.findByTeaId(teacher.getTeaId());
                map.put(STATE, teacher.getState() == 1 ? "활성화" : "비활성화");
                map.put(ID, teacher.getUid());
                map.put(NAME, teacher.getName());
                map.put(PK, teacher.getTeaId());
                if (c != null) {
                    String[] gradeClass = Parser.classParserArray(Long.toString(c.getClassId()));
                    map.put(GRADE, gradeClass[0]);
                    map.put(CLASS, gradeClass[1]);
                    Long studentClass = studentClassRepository.countByClassId(c);
                    map.put(TOTAL_STUDENT, studentClass);
                }
                map.put(CREATED_AT, Date.valueOf(teacher.getCreatedAt().toLocalDate()));
                list.add(map);
            }
        } else {
            throw new CustomException(DIVISION_ERROR);
        }
        return list;
    }


    @Transactional
    public void updateUser(UpdateUserReq p) {
        int result=0;
        if (p.getUserClass() != 0 && p.getUserGrade() != 0){
            String aString = Integer.toString(p.getUserGrade());
            String bFormatted = String.format("%02d", p.getUserClass());
            String resultString = aString + bFormatted;
            result = Integer.parseInt(resultString);
        }
        log.info("result: {}", result) ;


//        부모
        if (p.getP() == PrarentCode) {
            Optional<Parents> optionalParent = parentRepository.findById(p.getPk());
            if (optionalParent.isEmpty()) {
                throw new CustomException(NOT_FOUND_USER_ERROR);
            }
            Parents parent = parentRepository.getReferenceById(p.getPk());
            parent.setState(p.getState());
            parentRepository.save(parent);
        }
//        교직원
        else if (p.getP() == TeacherCode) {
            Optional<Teacher> optionalTeacher = teacherRepository.findById(p.getPk());
            if (optionalTeacher.isEmpty()) {
                throw new CustomException(NOT_FOUND_USER_ERROR);
            }
            Teacher teacher = teacherRepository.getReferenceById(p.getPk());
            if(p.getState() != 0) {
                teacher.setState(p.getState());
            }
            if(p.getUserName() != null){
                teacher.setName(p.getUserName());
            }
            if (result != 0){
                Optional<Class> optionalClass = classRepository.findById(result) ;
                log.info("optionalClass: {}", optionalClass);

                if (optionalClass.isPresent()) {
                    // 학급 데이터가 존재하고, TeaId가 존재하는 경우 예외 처리
                    Class existingClass = optionalClass.get();
                    if (existingClass.getTeaId() != null) {
                        throw new CustomException(MULTIPLE_TEACHER_ERROR);
                    }
                    // 기존 학급에 교사를 할당
                    existingClass.setTeaId(teacher) ;
                    classRepository.save(existingClass);
                    // 학급 데이터가 존재하지만 TeaId가 없으면 (필요한 처리) 추가 로직을 여기에서 구현
                } else { // 기존 학급 업데이트
                    int delClass = mapper.delClassTeacher(p.getPk());
                    log.info("delClass: {}", delClass);
                    // 학급 데이터가 존재하지 않으면 새로운 학급 데이터 생성 및 저장
                    Class newClass = new Class();
                    newClass.setClassId(result);
                    newClass.setTeaId(teacher);
                    classRepository.save(newClass);
                }
            }
            teacherRepository.save(teacher);
        } else if (p.getP() == StudentCode) {
            Optional<Student> optionalStudent = studentRepository.findById(p.getPk());
            if (optionalStudent.isEmpty()) {
                throw new CustomException(NOT_FOUND_USER_ERROR);
            }
            Student student = studentRepository.getReferenceById(p.getPk());
            if(p.getState() != 0) {
                student.setState(p.getState());
            }
            if(p.getUserName() != null){
                student.setName(p.getUserName());
            }
            if (result != 0){
                StudentClass data = studentClassRepository.findByClassIdAndStuId(result,student.getStuId());
                if(data == null){

                    data = new StudentClass();

                    Optional<Class> newClass = classRepository.findById(result);
                    if(newClass.isEmpty()) {
                        throw new CustomException(NOT_FOUND_CLASS_ERROR);
                    }

                    data.setClassId(newClass.get());
                    data.setStuId(student);



//                    학생 grade 업데이트 처리

                    StudentClass maxStudentNum = studentClassRepository.findTopByClassIdOrderByScId(result);
                    log.info("maxStudentNum:"+maxStudentNum);
                    if(maxStudentNum != null){
                        Student maxStudentGrade = studentRepository.getReferenceById(maxStudentNum.getStuId().getStuId());

                        log.info("maxStudentGrade:"+maxStudentGrade);
                        student.setGrade(maxStudentGrade.getGrade()+1);
                    }
                    else {
                        String aString = Integer.toString(result);
                        String bFormatted = String.format("01");
                        String resultString = aString + bFormatted;
                        result = Integer.parseInt(resultString);
                        student.setGrade(result);
                    }
                    studentClassRepository.save(data);
                }
            }
            studentRepository.save(student);
        } else {
            throw new CustomException(DIVISION_ERROR);
        }
    }
}
