package com.green.fefu.online;

import com.green.fefu.entity.Parents;
import com.green.fefu.entity.Student;
import com.green.fefu.entity.Teacher;
import com.green.fefu.exception.CustomException;
import com.green.fefu.parents.repository.ParentRepository;
import com.green.fefu.security.AuthenticationFacade;
import com.green.fefu.security.MyUser;
import com.green.fefu.student.repository.StudentRepository;
import com.green.fefu.teacher.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.green.fefu.exception.JSH.JshErrorCode.CAN_T_GET_GRADE;
import static com.green.fefu.exception.JSH.JshErrorCode.STUDENT_PK_NOT_FOUND_ERROR;
@Component
@RequiredArgsConstructor
@Slf4j
public class CommonMethodStorage {
    private final ParentRepository parentRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    private final OnlineMapper mapper;


    public Long signedUserGrade(MyUser user, Long studentPk) {
        Long grade = switch (user.getRole()) {
            case "ROLE_PARENTS" -> {
                if (studentPk != null && studentPk == 0) {
                    throw new CustomException(STUDENT_PK_NOT_FOUND_ERROR);
                }
                Parents parents = parentRepository.getReferenceById(user.getUserId());
                yield mapper.parentsClass(parents.getParentsId(), studentPk);
            }
            case "ROLE_TEACHER" -> {
                Teacher teacher = teacherRepository.getReferenceById(user.getUserId());
                yield mapper.teacherClass(teacher.getTeaId());
            }
            case "ROLE_STUDENT" -> {
                Student student = studentRepository.getReferenceById(user.getUserId());
                yield mapper.studentClass(student.getStuId());
            }
            default -> throw new CustomException(CAN_T_GET_GRADE);
        };
        return grade;


//        =============== 기존 메소드 ===============
//        MyUser user=authenticationFacade.getLoginUser();
//        long grade = switch (user.getRole()){
//            case "ROLE_PARENTS"->{
//                if(p.getStudentPk() != null || p.getStudentPk()==0){
//                    throw new CustomException(STUDENT_PK_NOT_FOUND_ERROR);
//                }
//                Parents parents=parentRepository.getReferenceById(user.getUserId());
//                yield mapper.parentsClass(parents.getParentsId(), p.getStudentPk());
//            }
//            case "ROLE_TEACHER"-> {
//                Teacher teacher = teacherRepository.getReferenceById(user.getUserId());
//                yield mapper.teacherClass(teacher.getTeaId());
//            }
//            case "ROLE_STUDENT"->{
//                Student student=studentRepository.getReferenceById(user.getUserId());
//                yield mapper.studentClass(student.getStuId());
//            }
//            default-> throw new CustomException(CAN_T_GET_GRADE);
//        };

    }
}
