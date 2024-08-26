package com.green.fefu.notice;

import com.green.fefu.entity.Notice;
import com.green.fefu.entity.Teacher;
import com.green.fefu.exception.CustomException;
import com.green.fefu.exception.CustomErrorCode;
import com.green.fefu.notice.model.*;
import com.green.fefu.security.AuthenticationFacade;
import com.green.fefu.security.MyUser;
import com.green.fefu.student.repository.ClassRepository;
import com.green.fefu.teacher.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.green.fefu.entity.Class;
import java.util.*;

import static com.green.fefu.exception.JSH.JshErrorCode.*;
import static java.time.LocalDateTime.now;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService{
    private final NoticeMapper mapper;
    private final AuthenticationFacade authenticationFacade; //PK값을 제공(getLoginUserId();
    private final NoticeRepository repository;
    private final TeacherRepository teacherRepository;
    private final ClassRepository classRepository;

    /*알림장 등록 : 권한있는 사람만 등록 가능*/
    public int postNotice(PostNoticeReq p){
        Teacher teacher = teacherRepository.getReferenceById(authenticationFacade.getLoginUserId());
        Class classes=classRepository.findByTeaId(authenticationFacade.getLoginUserId());
        if(classes==null){
            throw new CustomException(HOMEROOM_ISN_T_EXIST);
        }

        MyUser myUser=authenticationFacade.getLoginUser();
        log.info("user's role: {}", myUser.getRole());
        if(!(myUser.getRole().equals("ROLE_TEACHER"))){
            throw new CustomException(CustomErrorCode.YOU_ARE_NOT_TEACHER);
        }
        log.info("classroom :{}",classes.getClassId());
        log.info("{}", p.getState());
        if(!(p.getState()==1 || p.getState()==2)){
            throw new CustomException(NOTICE_STATE_CHECK);
        }
        Notice notice=new Notice();
        notice.setTeacher(teacher);
        notice.setTitle(p.getTitle());
        notice.setContent(p.getContent());
        notice.setClasses(classes);
        notice.setState(p.getState());
        notice.setCreatedAt(now());

        repository.save(notice);

        return 1;
    }


    public Map<String, List<GetNoticeRes>> getNotice(GetNoticeReq p){
        MyUser user=authenticationFacade.getLoginUser();
        log.info("pk : {}", authenticationFacade.getLoginUser().getRole());
        String userRole=user.getRole();
        if(userRole.equals("ROLE_TEACHER")){
            long teaId=authenticationFacade.getLoginUserId();
            int classId=mapper.teacherHomeroom(teaId);
            p.setClassId(classId);
            List<GetNoticeRes> allList=mapper.getNotice(p);
            return noticeType(allList);
        }else if(userRole.equals("ROLE_PARENT")) {
            long parentsId = authenticationFacade.getLoginUserId();
            Integer classId = mapper.childClassRoomList(parentsId, p.getStudentPk());
            if (classId == null) {
                throw new CustomException(HAS_NOT_GRADE);
            }
            p.setClassId(classId);
            List<GetNoticeRes> allList = mapper.getNotice(p);
            return noticeType(allList);
        }
        long stuId=authenticationFacade.getLoginUserId();
        Integer classId=mapper.studentClass(stuId);
        p.setClassId(classId);
        List<GetNoticeRes> allList=mapper.getNotice(p);
        return noticeType(allList);
    }


    //최신의 알림장 정보 1개 조회
    public Map<String, GetNoticeRes> getNoticeLatest(GetNoticeReq p){
        MyUser user=authenticationFacade.getLoginUser();
        String userRole=user.getRole();
        if(userRole.equals("ROLE_TEACHER")){
            long teaId=authenticationFacade.getLoginUserId();
            Integer classId=mapper.teacherHomeroom(teaId);
            if(classId==null){
                throw new CustomException(HAS_NOT_GRADE);
            }
            p.setClassId(classId);
            List<GetNoticeRes> allList=mapper.getNoticeLatest(p);
            return noticeTypeOne(allList);
        }else if(userRole.equals("ROLE_PARENT")) {
            long parentsId = authenticationFacade.getLoginUserId();
            Integer classId = mapper.childClassRoomList(parentsId, p.getStudentPk());
            if(classId==null){
                throw new CustomException(HAS_NOT_GRADE);
            }
            p.setClassId(classId);
            List<GetNoticeRes> allList = mapper.getNoticeLatest(p);
            return noticeTypeOne(allList);
        }
        long stuId=authenticationFacade.getLoginUserId();
        Integer classId=mapper.studentClass(stuId);
        if(classId==null){
            throw new CustomException(HAS_NOT_GRADE);
        }
        p.setClassId(classId);
        List<GetNoticeRes> allList=mapper.getNoticeLatest(p);
        return noticeTypeOne(allList);
    }

    Map<String, List<GetNoticeRes>> noticeType(List<GetNoticeRes> allList){
        List<GetNoticeRes> state1=new ArrayList();
        List<GetNoticeRes> state2=new ArrayList();

        for(GetNoticeRes res:allList){
            if(res.getState()==1){
                state1.add(res);
            }else{
                state2.add(res);
            }
        }
        Map<String, List<GetNoticeRes>> map=new LinkedHashMap();
        map.put("notice", state1);
        map.put("item", state2);
        return map;
    }

    Map<String, GetNoticeRes> noticeTypeOne(List<GetNoticeRes> allList){
        GetNoticeRes state1=null;
        GetNoticeRes state2=null;
        for(GetNoticeRes res:allList) {
            log.info("STATE 값 조회 for문 시작 : {}", res.getState());
            if (state1 == null && res.getState() == 1) {
                state1 = res;
                log.info("state1 : {}", state1);
            }
            log.info("state2-1 : {}", state2);
            log.info("STATE 값 조회 : {}", res.getState());
            if(state2 == null && res.getState() == 2){
                state2 = res;
                log.info("state2-2 : {}", state2);
            }
            log.info("state2-3 : {}", state2);
        }
        Map<String, GetNoticeRes> map=new LinkedHashMap();
        map.put("notice", state1);
        map.put("item", state2);
        return map;
    }


    public int putNotice(PutNoticeReq p){ //구현 예정
        p.setTeaId(authenticationFacade.getLoginUserId());
        return mapper.putNotice(p);
    }

    public int deleteNotice(DeleteNoticeReq p){
        log.info("ser1: {}", p);
        p.setTeaId(authenticationFacade.getLoginUserId());
        p.setClassId(mapper.teacherHomeroom(p.getTeaId()));
        log.info("ser2: {}", p);
        return mapper.deleteNotice(p);
    }
}
