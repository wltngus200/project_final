package com.green.fefu.online;

import com.green.fefu.common.CustomFileUtils;
import com.green.fefu.entity.*;
import com.green.fefu.exception.CustomException;
import com.green.fefu.online.model.*;
import com.green.fefu.online.repository.OnlineEnglishListeningRepository;
import com.green.fefu.online.repository.OnlineEnglishWordRepository;
import com.green.fefu.parents.repository.ParentRepository;
import com.green.fefu.security.AuthenticationFacade;
import com.green.fefu.security.MyUser;
import com.green.fefu.student.repository.StudentRepository;
import com.green.fefu.teacher.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.green.fefu.exception.JSH.JshErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OnlineEnglishServiceImpl {
    private final CommonMethodStorage methodStorage;

    // 내가 저장하고 꺼내올  Entity Repository
    private final OnlineEnglishWordRepository wordRepository;
    private final OnlineEnglishListeningRepository listeningRepository;

    // 파일을 다루고, 로그인 된 사용자
    private final AuthenticationFacade authenticationFacade;

    // 기타 객체
    private final CustomFileUtils customFileUtils;
    private final OnlineMapper mapper;

    // 어떠한 사용자인지 분기 -> 메소드화
    private final TeacherRepository teacherRepository;

    private final Integer TOTAL_TEST_QUESTION = 20;

    @Transactional // word DB 업로드
    public int postEnglishWordQuestion(PostOnlineQuestionEnglishWordReq p, MultipartFile pic) {
        log.info("service - p {}", p);
        log.info("service - pic {}", pic);
        OnlineEnglishWord entEnglishWord=new OnlineEnglishWord();
        entEnglishWord.setWord(p.getWord());
        entEnglishWord.setAnswer(p.getAnswer());
        String picName = customFileUtils.makeRandomFileName(pic);
        entEnglishWord.setPic(picName);
        Teacher teacher=teacherRepository.getReferenceById(authenticationFacade.getLoginUserId());
        entEnglishWord.setTeaId(teacher);
        Long teacherClass=mapper.teacherClass(teacher.getTeaId());
        if(teacherClass==null){
            throw new CustomException(HAS_NOT_CLASS);
        }
        entEnglishWord.setGrade(teacherClass);
        log.info("service-entity {}", entEnglishWord);
        log.info("service-entity.getGrade {}", entEnglishWord.getGrade());
        wordRepository.save(entEnglishWord);
        log.info("service-entity {}", entEnglishWord);


        //사진 처리
        if (pic != null || !(pic.isEmpty())) {
            try {
                String path = String.format("onlineEngWord/%s", entEnglishWord.getWordPk());
                customFileUtils.makeFolders(path);
                String target = String.format("%s/%s", path, picName);
                customFileUtils.transferTo(pic, target);
            } catch (Exception e) {
                e.printStackTrace();
                throw new CustomException(CAN_T_UPLOAD_QUESTION);
            }
        }else{
            log.info("else에 걸림");
        }

        return 1;
    }

    @Transactional
    public int PostEnglishListeningQuestion(PostOnlineQuestionEnglishListeningReq p, MultipartFile pic){
        log.info("service - p {}", p);
        log.info("service - pic {}", pic);
        OnlineEnglishListening entEnglishListening=new OnlineEnglishListening();
        entEnglishListening.setQuestion(p.getQuestion());
        entEnglishListening.setAnswer(p.getAnswer());
        String picName=customFileUtils.makeRandomFileName(pic);
        entEnglishListening.setSentence(p.getSentence());
        entEnglishListening.setPic(picName);
        log.info("service-entity {}", entEnglishListening);
        Teacher teacher=teacherRepository.getReferenceById(authenticationFacade.getLoginUserId());
        entEnglishListening.setTeaId(teacher);
        Long teaClass=mapper.teacherClass(teacher.getTeaId());
        if(teaClass==null){
            throw new CustomException(HAS_NOT_CLASS);
        }
        entEnglishListening.setGrade(teaClass);
        log.info("service-entity {}", entEnglishListening);
        listeningRepository.save(entEnglishListening);
        log.info("service-entity2 {}", entEnglishListening);


        //사진 처리
        if (pic != null || !pic.isEmpty()) {
            try {
                log.info("{}",entEnglishListening.getListeningPk());
                String path = String.format("onlineEngLis/%s",entEnglishListening.getListeningPk());
                customFileUtils.makeFolders(path);
                String target = String.format("%s/%s", path, picName);
                customFileUtils.transferTo(pic, target);
            } catch (Exception e) {
                e.printStackTrace();
                throw new CustomException(CAN_T_UPLOAD_QUESTION);
            }
        }
        return 1;
    }

    public List<GetEnglishWordQuestionRes> getEnglishWords(GetEnglishQuestionReq p){
        // 로그인 한 유저에 맞는 방식으로 학년 추출
        MyUser user = authenticationFacade.getLoginUser(); // ROLE를 구분해서 관련 학년정보 리턴
        Long grade=methodStorage.signedUserGrade(user, p.getStudentPk());
        if(grade==null){
            throw new CustomException(HAS_NOT_GRADE);
        }
        List<OnlineEnglishWord> listAll=wordRepository.getAllByGrade(grade);
        Collections.shuffle(listAll);
        List<GetEnglishWordQuestionRes> list=new ArrayList<>(20);
        for(int i=0; i<(5 < listAll.size() ? 5 : listAll.size()); i++){
            GetEnglishWordQuestionRes res=new GetEnglishWordQuestionRes();
            res.setWordQuePk(listAll.get(i).getWordPk());
            res.setWord(listAll.get(i).getWord());
            res.setAnswer(listAll.get(i).getAnswer());
            res.setPic(listAll.get(i).getPic());
            list.add(res);
        }
        return list;
    }

    public List<GetEnglishListeningQuestionRes> getEnglishListening(GetEnglishQuestionReq p){
        MyUser user = authenticationFacade.getLoginUser(); // ROLE를 구분해서 관련 학년정보 리턴
        Long grade=methodStorage.signedUserGrade(user, p.getStudentPk());
        if(grade==null){
            throw new CustomException(HAS_NOT_GRADE);
        }
        List<OnlineEnglishListening> listAll=listeningRepository.getAllByGrade(grade);
        Collections.shuffle(listAll);

        List<GetEnglishListeningQuestionRes> list=new ArrayList<>();
        for(int i=0; i<(TOTAL_TEST_QUESTION < listAll.size() ? TOTAL_TEST_QUESTION : listAll.size()); i++){
            OnlineEnglishListening listening=listAll.get(i);
            GetEnglishListeningQuestionRes res=new GetEnglishListeningQuestionRes();
            res.setQueId(listening.getListeningPk());
            res.setQuestion(listening.getQuestion());
            res.setSentence(listening.getSentence());
            res.setAnswer(listening.getAnswer());
            res.setPic(listening.getPic());
            list.add(res);
        }
        return list;
    }

}

