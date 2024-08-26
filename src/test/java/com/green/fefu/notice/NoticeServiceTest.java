package com.green.fefu.notice;

import com.green.fefu.notice.model.*;
import com.green.fefu.security.AuthenticationFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

 /*
    verify(); 실제 그 메소드가 작동 되었는지
    assertEquals(); 값이 같은지 확인
    given(); : 이 값이 주어졌을 때 willReturn()
    then();
*/


@ExtendWith(SpringExtension.class)
@Import({NoticeServiceImpl.class})
class NoticeServiceTest {
    private final int EFFECT=1;
    private final int MASTER_CLASS=502;
    private final long MASTER_TEACHER_PK=1000;

    @MockBean
    private NoticeMapper mapper;

    @Autowired
    private NoticeService service;

    @MockBean
    private AuthenticationFacade authenticationFacade;


    @Test
    void postNotice() {
        /*기본 세팅*/
        PostNoticeReq req1=new PostNoticeReq();
        req1.setTeaId(MASTER_TEACHER_PK);
        req1.setTitle("제목 100"); req1.setContent("내용 100");
        req1.setClassId(100); req1.setState(1);
        given(mapper.teacherHomeroom(req1.getTeaId())).willReturn(MASTER_CLASS);
        req1.setClassId(mapper.teacherHomeroom(req1.getTeaId()));

        /*가짜 임무 부여 및 확인+실제 메소드 전달 확인*/
        given(mapper.postNotice(req1)).willReturn(EFFECT);
        int answer=service.postNotice(req1);
        assertEquals(EFFECT, answer,"영향받은 행이 다름");
        verify(mapper).postNotice(req1);
    }

    @Test
    void getNotice() {
        /*가짜 리스트*/
        List<GetNoticeRes> list1=new ArrayList();
        GetNoticeRes res1=new GetNoticeRes();
        GetNoticeRes res2=new GetNoticeRes();
        res1.setNoticeId(1); res1.setTitle("가짜 1"); res1.setContent("가짜 1"); res1.setClassId(100);
        res2.setNoticeId(2); res2.setTitle("가짜 2"); res2.setContent("가짜 2"); res2.setClassId(100);
        list1.add(res1); list1.add(res2);

        /*가짜 임무 부여*/
        GetNoticeReq req1=new GetNoticeReq();
        req1.setState(1); req1.setClassId(100);
        given(mapper.getNotice(req1)).willReturn(list1);

        /*가짜 리스트 복제*/
        List<GetNoticeRes> mockList1=new ArrayList();
        GetNoticeRes mockRes1=new GetNoticeRes();
        GetNoticeRes mockRes2=new GetNoticeRes();
        mockRes1.setNoticeId(res1.getNoticeId()); mockRes1.setTitle(res1.getTitle());
        mockRes1.setContent(res1.getContent()); mockRes1.setClassId(res1.getClassId());
        mockRes2.setNoticeId(res2.getNoticeId()); mockRes2.setTitle(res2.getTitle());
        mockRes2.setContent(res2.getContent()); mockRes2.setClassId(res2.getClassId());
        mockList1.add(mockRes1); mockList1.add(mockRes2);

        List<GetNoticeRes> real= (List<GetNoticeRes>) service.getNotice(req1);
        verify(mapper).getNotice(req1);

        assertEquals(2, real.size(),"리턴 값 크기가 다름");
        assertEquals(mockList1.get(0), real.get(0),"0번 칸의 값이 다름");
        assertEquals(mockList1, real,"리턴 리스트 값이 다름");
    }


    @Test
    void putNotice() {
        PutNoticeReq req1=new PutNoticeReq();
        req1.setNoticeId(100); req1.setTitle("안녕"); req1.setContent("반가워"); req1.setTeaId(100);

        given(mapper.putNotice(req1)).willReturn(EFFECT);

        int effect=service.putNotice(req1);
        verify(mapper).putNotice(req1);

        assertEquals(EFFECT, effect, "영향 받은 행이 다름");
    }

    @Test
    void deleteNotice() {
        DeleteNoticeReq req1=new DeleteNoticeReq(100);
        given(mapper.deleteNotice(req1)).willReturn(EFFECT);
        int effect=service.deleteNotice(req1);
        verify(mapper).deleteNotice(req1);

        assertEquals(EFFECT, effect, "영향 받은 행이 다름");
    }
}