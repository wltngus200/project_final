package com.green.fefu.student.service;

import com.green.fefu.student.model.dto.*;
import com.green.fefu.student.model.req.createStudentReq;
import com.green.fefu.student.model.req.deleteStudentReq;
import com.green.fefu.student.model.req.updateStudentReq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StudentMapperTest {
    @Autowired
    private StudentMapper mapper;

    private final String msg = "원하는 값이 아닙니다.";
    private final String resultMsg = "값이 변경되지 않음";

    @Test
    @DisplayName("학생 생성")
    void createStudent() {
        createStudentReq p = new createStudentReq();
        p.setPic("a.jpg");
        p.setAddr("1234 # 서울 판교");
        p.setPhone("010-0000-0000");
        p.setGrade("10101");
        p.setBirth("2024-07-09");
        p.setName("홍길동");
        p.setEngName("hong");
        p.setEtc("갑각류 알러지 있음");
        p.setGender("남");
        int result = mapper.createStudent(p);
        assertEquals(1, result, resultMsg);
        getUserTest entity = mapper.selOneTest(p.getPk());
        assertEquals(p.getPic(), entity.getPic(), msg);
        assertEquals(p.getAddr(), entity.getAddr(), msg);
        assertEquals(p.getPhone(), entity.getPhone(), msg);
        assertEquals(p.getGrade(), entity.getGrade(), msg);
        assertEquals(p.getBirth(), entity.getBirth(), msg);
        assertEquals(p.getName(), entity.getName(), msg);
        assertEquals(p.getEngName(), entity.getEngName(), msg);
        assertEquals(p.getEtc(), entity.getEtc(), msg);
        assertEquals(p.getGender(), entity.getGender(), msg);
    }

    @Test
    @DisplayName("학생 삭제")
    void deleteStudent() {
        deleteStudentReq p = new deleteStudentReq();
        p.setPk(1L);
        p.setState(2);
        int result = mapper.deleteStudent(p);
        assertEquals(1, result, resultMsg);
        getUserTest entity = mapper.selOneTest(p.getPk());
        assertEquals(p.getState().toString(), entity.getState(), msg);

        deleteStudentReq p2 = new deleteStudentReq();
        p2.setPk(2L);
        p2.setState(3);
        result = mapper.deleteStudent(p2);
        assertEquals(1, result, resultMsg);
        getUserTest entity2 = mapper.selOneTest(p2.getPk());
        assertEquals(p2.getState().toString(), entity2.getState(), msg);
    }

    @Test
    @DisplayName("선생의 담당 학급 학생 리스트")
    void getStudentList() {
        List<getStudent> list = new ArrayList<>();
        getStudent p1 = new getStudent();
        p1.setName("유학생");
        p1.setGender("여");
        p1.setBirth("2009-01-01");
        p1.setPhone("010-9999-0000");
        p1.setParentName("유부모");
        p1.setParentPhone("010-1234-3456");
        getStudent p2 = new getStudent();
        p2.setName("노학생");
        p2.setGender("남");
        p2.setBirth("2008-12-12");
        p2.setPhone("010-0000-1111");
        p2.setParentName("노부모");
        p2.setParentPhone("010-2345-4567");

        list.add(p2);
        list.add(p1);

        List<getStudent> result = mapper.getStudentList(15L);
        assertEquals(2, result.size(), resultMsg);
        assertEquals(result.size(), list.size(), resultMsg);
        for (int i = 0; i < list.size(); i++) {
            assertEquals(result.get(i).getName(), list.get(i).getName(), msg);
            assertEquals(result.get(i).getGender(), list.get(i).getGender(), msg);
            assertEquals(result.get(i).getBirth(), list.get(i).getBirth(), msg);
            assertEquals(result.get(i).getPhone(), list.get(i).getPhone(), msg);
            assertEquals(result.get(i).getParentName(), list.get(i).getParentName(), msg);
            assertEquals(result.get(i).getParentPhone(), list.get(i).getParentPhone(), msg);
        }
    }

    @Test
    @DisplayName("학생 상세 정보")
    void getStudentDetail() {
        getDetail p = new getDetail();
        p.setPk("1");
        p.setName("홍길동");
        p.setBirth("2010-05-15");
        p.setGender("남");
        p.setPhone("010-0000-0000");
        p.setAddr("1234 # 서울 판교로 112");
        p.setEtc("갑각류 알러지 있음");
        p.setCreatedAt("2024-07-04 08:02:22");
        p.setParentId("parent1");
        p.setUClass("201");
        p.setParentName("김부모");
        p.setConnet("부");
        p.setParentPhone("010-1234-5678");
        p.setPic("47872175-b41f-4080-bcf9-dc72604c46d5.png");
        p.setTeacherName("정선생");
        getDetail result = mapper.getStudentDetail(1L);
        assertEquals(p, result);
        assertNotNull(result);

    }

    @Test
    @DisplayName("학생 정보 변경")
    void updateStudent() {
        updateStudentReq p = new updateStudentReq();
        p.setStudentPk(1);
        p.setStudentName("강길동");
        int result = mapper.updateStudent(p);
        assertEquals(1, result, resultMsg);
        getUserTest entity = mapper.selOneTest(p.getStudentPk());
        assertEquals(p.getStudentName(), entity.getName(), msg);
    }

    @Test
    @Transactional
    public void testUpdateStudent_OptionalFieldsMissing() {
        // Given
        updateStudentReq p = new updateStudentReq();
        p.setStudentPk(1);
        p.setStudentName("강길동");
        p.setStudentAddr("서울 판교로 112");
        p.setStudentZoneCode("1234");
        p.setFullAddr(); // fullAddr 설정

        // When
        int result = mapper.updateStudent(p);

        // Then
        assertEquals(1, result, "Update result should be 1");

        // Fetch updated entity
        getUserTest entity = mapper.selOneTest(p.getStudentPk());

        // Verify updated fields
        assertEquals(p.getStudentName(), entity.getName(), "Name should be updated");
        assertEquals(p.getFullAddr(), entity.getAddr(), "Full address should be updated");
    }

    @Test
    @Transactional
    public void testUpdateStudent_NonExistentPk() {
        // Given
        updateStudentReq p = new updateStudentReq();
        p.setStudentPk(999); // Non-existent pk
        p.setStudentName("강길동");
        p.setStudentAddr("서울 판교로 112");
        p.setStudentZoneCode("1234");
        p.setStudentPhone("010-0000-0000");
        p.setStudentEtc("갑각류 알러지 있음");
        p.setFullAddr(); // fullAddr 설정

        // When
        int result = mapper.updateStudent(p);

        // Then
        assertEquals(0, result, "Update result should be 0 for non-existent pk");
    }

    @Test
    @DisplayName("부모 없는 학생 리스트")
    void getStudentListForParent() {
        getListForNoParent p1 = new getListForNoParent();
        p1.setPk(24);
        p1.setName("홍길동");
        p1.setGrade("10101");
        List<getListForNoParent> list = new ArrayList<>();
        list.add(p1);

        List<getListForNoParent> result = mapper.getStudentListForParent(p1.getName());
        assertEquals(list.size(), result.size(), resultMsg);
        for (int i = 0; i < list.size(); i++) {
            assertEquals(result.get(i).getName(), list.get(i).getName(), msg);
            assertEquals(result.get(i).getGrade(), list.get(i).getGrade(), msg);
            assertEquals(result.get(i).getPk(), list.get(i).getPk(), msg);
        }
    }

    @Test
    @DisplayName("현재 etc 값 가져오기")
    void getStudentEtc() {
        getUserTest entity = mapper.selOneTest(1);
        String a = mapper.getStudentEtc(1);
        assertEquals(entity.getEtc(), a);
    }

    @Test
    @DisplayName("etc 값 업데이트")
    void updStudentEtc() {
        getUserTest entity = mapper.selOneTest(1);
        assertEquals("갑각류 알러지 있음", entity.getEtc());
        mapper.updStudentEtc(1, "바꿈");

    }

    @Test
    @DisplayName("역대 etc 값 받아오기")
    void selPrevEtc() {
        prevStudentEtc p = new prevStudentEtc();
        prevStudentEtc p1 = new prevStudentEtc();
        List<prevStudentEtc> list = new ArrayList<>();
        List<prevStudentEtc> result = mapper.selPrevEtc(1);
        p.setUClass("101");
        p.setEtc(null);
        p.setTeacherName("홍길동");
        list.add(p);
        p1.setUClass("201");
        p1.setEtc("새우 알러지");
        p1.setTeacherName("정선생");
        list.add(p1);
        assertEquals(2, result.size(), resultMsg);
        assertEquals(list, result);
    }

    @Test
    @DisplayName("부모 회원가입 시 학생 에 fk 업데이트")
    void updStudentParent() {
        getUserTest entity = mapper.selOneTest(2);
        assertNull(entity.getParentId(), "널이 아닙니다.");
        mapper.updStudentParent(2, 1);
        entity = mapper.selOneTest(2);
        assertEquals("1", entity.getParentId(), "값이 들어가지 않았습니다");
    }
}