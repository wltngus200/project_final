package com.green.fefu.student.test;

import com.green.fefu.student.model.dto.getListForNoParent;
import com.green.fefu.student.model.dto.getStudent;
import com.green.fefu.student.model.req.createStudentReq;
import com.green.fefu.student.model.req.deleteStudentReq;
import com.green.fefu.student.model.req.updateStudentReq;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface StudentService {
    Map createStudent(createStudentReq p, MultipartFile pic, Map map);
    void deleteStudent(deleteStudentReq p);
    List getStudentList(List<getStudent> list);
    Map getStudentDetail(long pk, Map map);
    void updateStudent(updateStudentReq p , MultipartFile pic);
    Map getStudentListForParent(String searchWord);
}