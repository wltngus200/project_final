package com.green.fefu.admin.service;

import com.green.fefu.admin.model.dto.GetUserListDto;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AdminMapperTest {
    @Autowired
    private AdminMapper mapper;
    @Test
    void getParentList() {
        List<GetUserListDto> list = new ArrayList<>();
        List<GetUserListDto> result = mapper.getParentList("");
        list.add(createDto("parent4", "4", "최부모", "102", "2024-07-04 08:02:22"));
        list.add(createDto("parent5", "5", "정부모", "101", "2024-07-04 08:02:22"));
        list.add(createDto("parent8", "8", "윤부모", "202", "2024-07-04 08:02:22"));
        list.add(createDto("parent11", "11", "홍부모", "103", "2024-07-04 08:17:36"));
        list.add(createDto("parent14", "14", "신부모", "104", "2024-07-04 08:17:36"));
        list.add(createDto("parent17", "17", "권부모", "204", "2024-07-04 08:17:36"));
        list.add(createDto("parent19", "19", "유부모", "303", "2024-07-04 08:17:36"));
        list.add(createDto("xptmxmid1", "53", "김철수", null, "2024-07-11 09:39:37"));
        list.add(createDto("1231asd", "55", "지김시", null, "2024-07-11 09:42:14"));
        list.add(createDto("gkrqna1h1", "61", "samd", null, "2024-07-11 10:51:04"));
        list.add(createDto("gkrqna1h11", "70", "samkim", null, "2024-07-11 10:53:23"));
        list.add(createDto("gkrq21", "72", "samkim", null, "2024-07-11 10:54:36"));
        list.add(createDto("gkr12314q21", "74", "samkim", null, "2024-07-11 11:29:27"));
        list.add(createDto("g4q121", "79", "김이박", null, "2024-07-11 11:34:12"));
        list.add(createDto("g4q1121", "80", "김이박", null, "2024-07-11 11:34:34"));
        list.add(createDto("g4q1121214", "97", "김이박", null, "2024-07-11 14:46:50"));
        list.add(createDto("g4q1121asda", "101", "김이박", null, "2024-07-11 15:01:07"));
        list.add(createDto("parenttest1", "158", "박부모", null, "2024-07-11 16:28:46"));
        list.add(createDto("testids12", "174", "길동이", null, "2024-07-11 17:08:06"));

        assertEquals(19, result.size(), "개수가 다름");
        for (int i = 0; i < list.size(); i++) {
            assertEquals(list.get(i), result.get(i));
        }
    }
    private static GetUserListDto createDto(String id, String pk, String name, String grade, String createdAt) {
        GetUserListDto dto = new GetUserListDto();
        dto.setId(id);
        dto.setPk(pk);
        dto.setName(name);
        dto.setGrade(grade);
        dto.setCreatedAt(createdAt);
        return dto;
    }

    @Test
    void getTeacherList() {
        List<GetUserListDto> list = new ArrayList<>();
        List<GetUserListDto> result = mapper.getTeacherList("");
        list.add(createDto("teacher2", "2", "최선생", "102", "2024-07-04 08:02:22"));
        list.add(createDto("teacher5", "5", "이선생", "301", "2024-07-04 08:02:22"));
        list.add(createDto("teacher7", "7", "조선생", "401", "2024-07-04 08:02:22"));
        list.add(createDto("teacher9", "9", "장선생", "501", "2024-07-04 08:02:22"));
        list.add(createDto("teacher19", "19", "백선생", "503", "2024-07-04 08:17:36"));
        list.add(createDto("teacher17", "17", "김선생", "403", "2024-07-04 08:17:36"));
        list.add(createDto("teacher15", "15", "류선생", "303", "2024-07-04 08:17:36"));
        list.add(createDto("teacher13", "13", "우선생", "203", "2024-07-04 08:17:36"));
        list.add(createDto("teacher99", "29", "백창", null, "2024-07-09 11:54:36"));
        list.add(createDto("teacher1122", "50", "강길동", "205", "2024-07-12 16:29:21"));

        assertEquals(list.size(), result.size(), "개수가 다름");
        for (int i = 0; i < list.size(); i++) {
            assertEquals(list.get(i), result.get(i));
        }
    }

    @Test
    void delParent() {
        int result = mapper.delParent(1);
        assertEquals(result, 1,"삭제되지 않음");
    }

    @Test
    void delTeacher() {
        int result = mapper.delTeacher(30);
        assertEquals(result, 1,"삭제되지 않음");
    }

    @Test
    void updParent() {
        int result = mapper.updParent(1);
        assertEquals(result, 1,"업데이트 되지 않음");
    }

    @Test
    void updTeacher() {
        int result = mapper.updTeacher(1);
        assertEquals(result, 1,"업데이트 되지 않음");
    }
}