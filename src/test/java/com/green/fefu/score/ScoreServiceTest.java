package com.green.fefu.score;

import com.green.fefu.score.model.*;
import com.green.fefu.semester.SemesterService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class) // spring 컨테이너를 사용하고 싶음 .직접
@Import({ScoreServiceImpl.class})
class ScoreServiceTest {
    @MockBean
    private  ScoreMapper mapper;
    @Autowired
    private ScoreServiceImpl service;

    @Test
    void postScore() {
        InsScoreReq p = new InsScoreReq();
        p.setScoreId(1);
        p.setStudentPk(1);
        p.setSemester(1);
        p.setYear("1");
        p.setExam(1);
        p.setName("김철수철수");
        p.setMark(1);
        given(mapper.postScore(p)).willReturn(1);
//        long res = service.postScore(p);
        assertEquals(1,service.postScore(p));
        verify(mapper,times(1)).postScore(p);
        System.out.println(p);

    }
    @Test
        void getScore_WithValidInput_ReturnsDtoWithMidtermScores() {
            // Arrange
            StuGetRes input = new StuGetRes();
            input.setStudentPk(1);
            input.setExam(1);

            StuGetRes mockedRes = new StuGetRes();
            mockedRes.setStudentPk(1);
            mockedRes.setLatestGrade(2);
            mockedRes.setLatestSemester(1);
            mockedRes.setLatestYear("2023");
            mockedRes.setExam(1);

            List<InsScoreList> mockedMidtermScores = Arrays.asList(new InsScoreList());

            when(mapper.getStu(1)).thenReturn(mockedRes);
            when(mapper.getScoreMidterm(mockedRes)).thenReturn(mockedMidtermScores);

            // Act
            Dto result = service.getScore(input);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.getStudentPk());
            assertEquals(2, result.getLatestGrade());
            assertEquals(1, result.getLatestSemester());
            assertEquals("2023", result.getLatestYear());
//            assertEquals(1, result.getExam());
            assertEquals(mockedMidtermScores, result.getList());
        }

        @Test
        void getScore_WithValidInputAndFinalExam_ReturnsDtoWithFinalScores() {
            // Arrange
            StuGetRes input = new StuGetRes();
            input.setStudentPk(1);
            input.setExam(2);

            StuGetRes mockedRes = new StuGetRes();
            mockedRes.setStudentPk(1);
            mockedRes.setLatestGrade(2);
            mockedRes.setLatestSemester(1);
            mockedRes.setLatestYear("2023");
            mockedRes.setExam(2);

            List<InsScoreList> mockedFinalScores = Arrays.asList(new InsScoreList());

            when(mapper.getStu(1)).thenReturn(mockedRes);
            when(mapper.getScoreFinal(mockedRes)).thenReturn(mockedFinalScores);

            // Act
            Dto result = service.getScore(input);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.getStudentPk());
            assertEquals(2, result.getLatestGrade());
            assertEquals(1, result.getLatestSemester());
            assertEquals("2023", result.getLatestYear());
//            assertEquals(2, result.getExam());
            assertEquals(mockedFinalScores, result.getList());
        }

        @Test
        void getScore_WithInvalidExam_ReturnsNull() {
            // Arrange
            StuGetRes input = new StuGetRes();
            input.setStudentPk(1);
            input.setExam(3);

            StuGetRes mockedRes = new StuGetRes();
            mockedRes.setExam(3);

            when(mapper.getStu(1)).thenReturn(mockedRes);

            // Act
            Dto result = service.getScore(input);

            // Assert
            assertNull(result);
        }

    @Test
    void getDetailScore_SemesterOneExamOne_Success() {
        // Arrange
        GetDetailScoreReq req = new GetDetailScoreReq();
        req.setStudentPk(1);
        req.setSemester(1);
        req.setExam(1);
        req.setGrade(3);

        StuGetRes stuGetRes = new StuGetRes();
        stuGetRes.setLatestGrade(3);

        List<InsScoreList> scoreDetails = Arrays.asList(new InsScoreList());

        when(mapper.getStu(req.getStudentPk())).thenReturn(stuGetRes);
        when(mapper.getDetailScore(req)).thenReturn(scoreDetails);

        // Act
        DtoDetail result = service.getDetailScore(req);

        // Assert
        assertNotNull(result);
        assertEquals(req.getStudentPk(), result.getStudentPk());
        assertEquals(scoreDetails, result.getList());
    }

    @Test
    void getDetailScore_SemesterTwoExamTwo_Success() {
        // Arrange
        GetDetailScoreReq req = new GetDetailScoreReq();
        req.setStudentPk(1);
        req.setSemester(2);
        req.setExam(2);
        req.setGrade(3);

        StuGetRes stuGetRes = new StuGetRes();
        stuGetRes.setLatestGrade(3);

        List<InsScoreList> scoreDetails = Arrays.asList(new InsScoreList());

        when(mapper.getStu(req.getStudentPk())).thenReturn(stuGetRes);
        when(mapper.getDetailScoreFinal(req)).thenReturn(scoreDetails);

        // Act
        DtoDetail result = service.getDetailScore(req);

        // Assert
        assertNotNull(result);
        assertEquals(req.getStudentPk(), result.getStudentPk());
        assertEquals(scoreDetails, result.getList());
    }

    @Test
    void getDetailScore_InvalidGrade_ThrowsException() {
        // Arrange
        GetDetailScoreReq req = new GetDetailScoreReq();
        req.setStudentPk(1);
        req.setSemester(1);
        req.setExam(1);
        req.setGrade(4);

        StuGetRes stuGetRes = new StuGetRes();
        stuGetRes.setLatestGrade(3);

        when(mapper.getStu(req.getStudentPk())).thenReturn(stuGetRes);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> service.getDetailScore(req), "잘못된 학년입니다.");
    }

    @Test
    void getDetailScore_NoScores_ThrowsException() {
        // Arrange
        GetDetailScoreReq req = new GetDetailScoreReq();
        req.setStudentPk(1);
        req.setSemester(1);
        req.setExam(1);
        req.setGrade(3);

        StuGetRes stuGetRes = new StuGetRes();
        stuGetRes.setLatestGrade(3);

        when(mapper.getStu(req.getStudentPk())).thenReturn(stuGetRes);
        when(mapper.getDetailScore(req)).thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> service.getDetailScore(req), "조회된 성적이 없습니다");
    }

    @Test
    void getDetailScore_InvalidSemesterExam_ReturnsEmptyList() {
        // Arrange
        GetDetailScoreReq req = new GetDetailScoreReq();
        req.setStudentPk(1);
        req.setSemester(3);
        req.setExam(3);
        req.setGrade(3);

        StuGetRes stuGetRes = new StuGetRes();
        stuGetRes.setLatestGrade(3);

        when(mapper.getStu(req.getStudentPk())).thenReturn(stuGetRes);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> service.getDetailScore(req), "조회된 성적이 없습니다");
    }
}