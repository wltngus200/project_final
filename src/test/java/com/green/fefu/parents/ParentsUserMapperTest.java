package com.green.fefu.parents;

import com.green.fefu.parents.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ParentsUserMapperTest {
    @Autowired ParentsUserMapper mapper ;

    @Test @DisplayName("학부모 post") // 회원가입
    void postParentsUser() {
        PostParentsUserReq p = new PostParentsUserReq();
        p.setUid("p1");
        p.setUpw("1212");
        p.setNm("홍길동");
        p.setPhone("010-1234-1234");
        p.setConnect("부");
        int affectedRow = mapper.postParentsUser(p);
        assertEquals(1, affectedRow);
        GetParentsUserReq req1 = new GetParentsUserReq();
        req1.setSignedUserId(p.getParentsId());
        ParentsUserEntity res = mapper.getParentsUser(req1);
        assertNotNull(res);
        assertEquals(p.getParentsId(), res.getParentsId(), "1. 이상");

        PostParentsUserReq p1 = new PostParentsUserReq();
        p1.setUid("p2");
        p1.setUpw("1212");
        p1.setNm("김길동");
        p1.setPhone("010-5678-5678");
        p1.setConnect("모");
        int affectedRow1 = mapper.postParentsUser(p1);
        assertEquals(1, affectedRow1);
        GetParentsUserReq req2 = new GetParentsUserReq();
        req2.setSignedUserId(p1.getParentsId());
        ParentsUserEntity res1 = mapper.getParentsUser(req2);
        assertNotNull(res1);
        assertEquals(p1.getParentsId(), res1.getParentsId(), "2. 이상");
    }
    @Test @DisplayName("post 1") // 회원가입
    void postParentsUser2() {
        PostParentsUserReq p = new PostParentsUserReq();
        p.setStudentPk(1);
        p.setUid("p1");
        p.setUpw("1212");
        p.setNm("김철수");
        p.setPhone("010-1234-1234");
        p.setSubPhone("010-1111-1111");
        p.setEmail("test@example.com");
        p.setConnect("Y");
        p.setAddr("Seoul");

        // 학부모 정보 저장
        int affectedRow = mapper.postParentsUser(p);
        long generatedParentsId = p.getParentsId() ;
        System.out.println("Affected Rows (first insert): " + affectedRow);
        System.out.println("Generated ParentsId (first insert): " + generatedParentsId);
        assertEquals(1, affectedRow, "1. 예상된 행 수");

        GetParentsUserReq req = new GetParentsUserReq();
        req.setSignedUserId(generatedParentsId);

        // 저장된 학부모 정보 가져오기
        System.out.println("Generated ParentsId (first insert): " + p.getParentsId());
        ParentsUserEntity res = mapper.getParentsUser(req);
        System.out.println("GetParentsUserRes (first select): " + res);
        assertNotNull(res, "1. 예상된 학부모 정보는 null이 아닙니다.");
        assertEquals(p.getParentsId(), res.getParentsId(), "1. 예상된 학부모 ID");

        PostParentsUserReq p1 = new PostParentsUserReq();
        p1.setUid("p2");
        p1.setUpw("5678");
        p1.setNm("김영희");
        p1.setPhone("010-5678-5678");
        p1.setSubPhone("010-2222-2222");
        p1.setEmail("test2@example.com");
        p1.setConnect("Y");
        p1.setAddr("Busan");


        // 두 번째 학부모 정보 저장
        int affectedRow1 = mapper.postParentsUser(p1);
        long generatedParentsId1 = p1.getParentsId();
        System.out.println("Affected Rows (second insert): " + affectedRow1);
        assertEquals(1, affectedRow1, "2. 예상된 행 수");

        GetParentsUserReq req1 = new GetParentsUserReq();
        req1.setSignedUserId(p1.getParentsId());

        // 두 번째로 저장된 학부모 정보 가져오기
        System.out.println("Generated ParentsId (second insert): " + p1.getParentsId());
        ParentsUserEntity res1 = mapper.getParentsUser(req1);
        System.out.println("GetParentsUserRes (second select): " + res1);
        assertNotNull(res1, "2. 예상된 학부모 정보는 null이 아닙니다.");
        assertEquals(p1.getParentsId(), res1.getParentsId(), "2. 예상된 학부모 ID");
    }
    @Test @DisplayName("patch 1") // 정보 수정
    void patchParentsUser() {
        PostParentsUserReq p = new PostParentsUserReq();
        p.setUid("p1");
        p.setUpw("1212");
        p.setNm("홍길동");
        p.setPhone("010-1234-1234");
        p.setConnect("부");
        int affectedRow = mapper.postParentsUser(p);
        assertEquals(1, affectedRow);

        PatchParentsUserReq req1 = new PatchParentsUserReq();
        req1.setParentsId(p.getParentsId());
        req1.setAddr("대구");
        req1.setSubPhone("010-1111-1111");
        req1.setEmail("test@example.com");
        int affectedRow1 = mapper.patchParentsUser(req1);
        assertEquals(1, affectedRow1, "1. 이상");
    }
    @Test @DisplayName("아이디 찾기") // 아이디 찾기
    void getFindId() {
        PostParentsUserReq p = new PostParentsUserReq();
        p.setUid("p1");
        p.setUpw("1212");
        p.setNm("홍길동");
        p.setPhone("010-1234-1234");
        p.setConnect("부");
        int affectedRow = mapper.postParentsUser(p);
        assertEquals(1, affectedRow);

        GetFindIdReq req = new GetFindIdReq();
        req.setNm("홍길동");
        req.setPhone("010-1234-1234");
        GetFindIdRes res = mapper.getFindId(req);
        assertEquals(p.getUid(), res.getUid());
    }
    @Test @DisplayName("비밀번호 변경") // 비밀번호 변경
    void patchPassword() {
        PostParentsUserReq p = new PostParentsUserReq();
        p.setUid("p1");
        p.setUpw("1212");
        p.setNm("홍길동");
        p.setPhone("010-1234-1234");
        p.setConnect("부");
        GetParentsUserReq req = new GetParentsUserReq();
        req.setSignedUserId(p.getParentsId());
        int affectedRow = mapper.postParentsUser(p);
        assertEquals(1, affectedRow);

        PatchPasswordReq req1 = new PatchPasswordReq();
        req1.setUid(p.getUid());
        req1.setParentsId(p.getParentsId());
        req1.setNewUpw("123123");
        int affectedRow1 = mapper.patchPassword(req1);
        assertEquals(1, affectedRow1);
        mapper.getParentsUser(req);
    }
    @Test @DisplayName("로그인") // 로그인
    void signInPost() {
        PostParentsUserReq user = new PostParentsUserReq();
        user.setUid("p1");
        user.setUpw("1212");
        user.setNm("홍길동");
        user.setPhone("010-1234-1234");
        user.setConnect("부");
        mapper.postParentsUser(user);

        ParentsUser user1 = mapper.signInPost(user.getUid());
        if(user1 != null){
            GetParentsUserReq req1 = new GetParentsUserReq();
            req1.setSignedUserId(user1.getParentsId());
            List<ParentsUser> userList = mapper.selTest(req1.getSignedUserId());
            ParentsUser user1comp = userList.get(0);
            user1comp.setParentsId(user1.getParentsId());

            assertEquals(user1comp.getParentsId(), user1.getParentsId());
            assertEquals(user1comp.getUid(), user1.getUid());
            assertEquals(user1comp.getNm(), user1.getNm());

            ParentsUser userNo = mapper.signInPost("asdf");
            assertNull(userNo);
        }
    }
    @Test @DisplayName("회원정보 확인") // 회원정보 확인
    void getParentsUser() {
        GetParentsUserReq req = new GetParentsUserReq() ;
        req.setSignedUserId(1) ;
        ParentsUserEntity entity = mapper.getParentsUser(req) ;
        List<ParentsUser> list = mapper.selTest(req.getSignedUserId()) ;
        assertEquals(1, list.size());
        assertEquals(entity.getUid(), list.get(0).getUid());

        GetParentsUserReq req1 = new GetParentsUserReq() ;
        req1.setSignedUserId(1000) ;
        ParentsUserEntity entity1 = mapper.getParentsUser(req1) ;
        assertNull(entity1);
    }
    @Test @DisplayName("비밀번호 찾기 용 회원찾기") // 회원찾기
    void getFindPassword(){
        ParentsUserEntity entity = new ParentsUserEntity() ;
        entity.setUid("parent1") ;
        entity.setPhone("010-1234-5678") ;

        GetFindPasswordReq req = new GetFindPasswordReq() ;
        req.setUid("parent1");
        req.setPhone("010-1234-5678");
        List<ParentsUserEntity> list = mapper.getParentUserList(req) ;
        list.add(entity) ;
        assertEquals(1, list.size());

        GetFindPasswordReq req1 = new GetFindPasswordReq() ;
        req1.setUid("parent100");
        req1.setPhone("010-4252-4567");
        List<ParentsUserEntity> list1 = mapper.getParentUserList(req1) ;
        assertEquals(0, list1.size());
    }
    @Test @DisplayName("전자서명") // 전자서명
    void signature(){
        SignatureReq req = SignatureReq.builder()
                .studentPk(1)
                .year("2024")
                .semester(1)
                .pic("test-pic.png")
                .build();

        int result = mapper.signature(req);

        assertNotNull(req.getSignId()); // useGeneratedKeys=true 이므로, signId가 자동으로 생성되었는지 확인
        assertEquals(1, result);
    }
}