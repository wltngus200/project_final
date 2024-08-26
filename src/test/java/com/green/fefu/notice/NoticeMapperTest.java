package com.green.fefu.notice;

import com.green.fefu.notice.model.*;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace/*testDB를 대체할지*/.NONE/*하지 않음*/)
//@ActiveProfiles("tdd")
class NoticeMapperTest {
    @Autowired //DI
    private NoticeMapper mapper;
    private final int DEFAULT_NUM=29;

    @Test
    void postNotice() {
        /*모든 열을 조회한 숫자가 일치하는지*/
        List<GetNoticeRes> all = mapper.getNoticeForTDD(); //20
        assertEquals(DEFAULT_NUM, all.size(),"기존 리스트의 값이 다름");

        /*영향 받은 행의 값이 1인지*/
        PostNoticeReq req1=new PostNoticeReq();
        req1.setTeaId(10); req1.setClassId(101); req1.setTitle("제목 100"); req1.setContent("내용 100"); req1.setState(1);
        //req1.setTeaId(20); req1.setClassId(501); req1.setTitle("제목 500"); req1.setContent("내용 500");
        int effect=mapper.postNotice(req1);
        assertEquals(1, effect, "영향 받은 행의 개수가 다름");

        /*기존 데이터에서 1이 추가 되는지*/
        List<GetNoticeRes> plusOne=mapper.getNoticeForTDD();
        assertEquals(DEFAULT_NUM +1, plusOne.size(), "기존데이터에서 1이 추가 되지 않음");

        /*마지막에 실제 그 데이터가 있는지*/
        GetNoticeRes res1=plusOne.get(DEFAULT_NUM); //리스트 0번부터 시작
        PostNoticeReq tmp=new PostNoticeReq();
        tmp.setTeaId(res1.getTeaId()); tmp.setClassId(res1.getClassId());
        tmp.setTitle(res1.getTitle()); tmp.setContent(res1.getContent());
        tmp.setState(res1.getState());
        assertEquals(tmp, req1, "넣은 값과 추가된 값이 다름");
    }

    @Test
    void getNotice() {
        /*이상한 번호를 넣었을 때 조회되지 않음*/
        GetNoticeReq req1=new GetNoticeReq();
        req1.setClassId(486); req1.setState(0);
        List<GetNoticeRes> res1=mapper.getNotice(req1);
        assertEquals(0, res1.size(), "조회되는 행이 있음");

        /*특정 값을 넣었을 때 n개 조회*/
        GetNoticeReq req2=new GetNoticeReq();
        req2.setClassId(501); req2.setState(1);
        List<GetNoticeRes> res2=mapper.getNotice(req2);
        assertEquals(1, res2.size(), "조회된 행의 개수가 다름");

        /*전체 조회 = TDD 전용*/
        List<GetNoticeRes> resAll=mapper.getNoticeForTDD();
        assertEquals(DEFAULT_NUM, resAll.size(), "전체 행의 개수가 다름");

        /*n번 행이 실제 그 행인지*/
        GetNoticeRes onePick=resAll.get(0);
        assertEquals(1, onePick.getNoticeId(), "PK 값이 다름");
        assertEquals(1, onePick.getTeaId(), "선생님의 PK 값이 다름");
        assertEquals("1학기 학부모 상담 안내", onePick.getTitle(), "제목이 다름");
        assertEquals("다음 주 화요일부터 1학기 학부모 상담이 시작됩니다. 일정을 확인해 주세요.", onePick.getContent(), "내용이 다름");
        assertEquals(101, onePick.getClassId(), "반의 PK 값이 다름");
    }

    @Test
    void putNotice() {
        /*영향받은 행의 값이 1인지*/
        PutNoticeReq req1=new PutNoticeReq();
        req1.setNoticeId(7); req1.setTitle("제목 변경");
        req1.setContent("내용 변경"); req1.setTeaId(7);

        int effect=mapper.putNotice(req1);
        assertEquals(1, effect, "영향 받은 행이 1이 아님");

        /*실제 수정된 내용으로 조회했을 때 1개인지*/
        GetOneNoticeForTDD reqOne1=new GetOneNoticeForTDD();
        reqOne1.setTitle("제목 변경");
        reqOne1.setContent("내용 변경");

        List<GetNoticeRes> putOne=mapper.getNoticeForTDDJustOne(reqOne1);
        assertEquals(1, putOne.size(), "같은 내용의 다른의 행이 조회됨");

        /*그 조회된 1개가 수정과 내용이 같은지*/
        assertEquals(reqOne1.getTitle(), putOne.get(0).getTitle(), "제목이 수정되지 않음");
        assertEquals(reqOne1.getContent(), putOne.get(0).getContent(), "내용이 수정되지 않음");

        /*행의 수에 변화는 없는지*/
        List<GetNoticeRes> allNotice=mapper.getNoticeForTDD();
        assertEquals(DEFAULT_NUM, allNotice.size(), "행이 추가되거나 삭제되었음");


        /*수정 전 행이 사라졌는지- Not*/
        for(GetNoticeRes res:allNotice) {
            assertNotEquals("학부모회 모임 안내", res.getTitle(), "제목이 다름");
        }
    }

    @Test
    void deleteNotice() {
        /*전체 행의 개수를 확인*/
        List<GetNoticeRes> allRes=mapper.getNoticeForTDD();
        assertEquals(DEFAULT_NUM, allRes.size(), "전체 행의 개수가 다름");

        /*영향받은 행이 1인지*/
        DeleteNoticeReq req1 = new DeleteNoticeReq(42);
        req1.setTeaId(1);
        req1.setClassId(mapper.teacherHomeroom(req1.getTeaId()));
        int effect = mapper.deleteNotice(req1);
        assertEquals( 1, effect, "영향 받은 행이 다름");

        /*전체 행의 개수가 달라졌는지*/
        List<GetNoticeRes> allRes2=mapper.getNoticeForTDD();
        assertEquals(DEFAULT_NUM-1, allRes2.size(),"행의 변화가 이상함");

        /*지운 행이 없는지*/
        for(GetNoticeRes res: allRes2){
            assertNotEquals(res.getNoticeId(), req1.getNoticeId(), "지워진 행의 PK값이 조회됨");
        }
    }

    @Test
    void teacherHomeroom() {
        int answer1=mapper.teacherHomeroom(1);
        assertEquals(answer1, 101, "다른 학급이 조회됨");

        int answer2=mapper.teacherHomeroom(12);
        assertEquals(answer2, 104, "다른 학급이 조회됨");

        int answer3=mapper.teacherHomeroom(3);
        assertEquals(answer3, 201, "다른 학급이 조회됨 ");

    }
}