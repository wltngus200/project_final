package com.green.fefu.online;

import com.green.fefu.common.CustomFileUtils;
import com.green.fefu.entity.*;
import com.green.fefu.entity.dummy.Subject;
import com.green.fefu.entity.dummy.TypeTag;
import com.green.fefu.exception.CustomException;
import com.green.fefu.online.model.*;
import com.green.fefu.online.repository.*;
import com.green.fefu.security.AuthenticationFacade;
import com.green.fefu.security.MyUser;
import com.green.fefu.student.repository.StudentRepository;
import com.green.fefu.teacher.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

import static com.green.fefu.exception.JSH.JshErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class HaesolOnlineServiceImpl {
    // 공통된 메소드를 저장(로그인한 유저에 따라 해당하는 학년정보 추출)
    private final CommonMethodStorage methodStorage;

    // 내가 저장하고 꺼내올  Entity Repository
    private final HaesolOnlineRepository haesolOnlineRepository;
    private final HaesolOnlineMultipleRepository haesolOnlineMultipleRepository;
    private final TypeTagRepository typeTagRepository;
    private final SubjectRepository subjectRepository;

    // 어떠한 사용자인지 분기 -> 메소드화
    private final AuthenticationFacade authenticationFacade;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final StudentOnlineRecodeRepository studentOnlineRecodeRepository;
    private final StudentOnlineTestRepository studentOnlineTestRepository;

    // 기타 문제
    private final CustomFileUtils customFileUtils;
    private final OnlineMapper mapper;
    private final Integer TOTAL_TEST_QUESTION = 20;

    //========= 국어 및 수학 문제 출제 =========
    @Transactional
    public int PostKorAMatQuestion(MultipartFile pic, PostOnlineQuestionReq p) {
        log.info("Service 데이터 객체 : {}", p);
        //setter에 들어갈 Entity + fileName제작
        Teacher teacher = teacherRepository.getReferenceById(authenticationFacade.getLoginUserId());
        log.info("teacher entity {}", teacher);
        Subject subject = subjectRepository.getReferenceById(p.getSubjectCode());
        log.info("subject entity {}", subject);
        TypeTag typeTag = typeTagRepository.findByTypeNum(p.getTypeTag());
        log.info("타입태그 확인 {}", p.getTypeTag());
        log.info("p.getSubjectCode {}", p.getSubjectCode());
        log.info("typeTag entity {}", typeTag);
        String picName = customFileUtils.makeRandomFileName(pic);

        // entity 생성 + 파일 이름
        HaesolOnline haesolOnline = new HaesolOnline();

        haesolOnline.setTeaId(teacher); //선생님 PK->아래에서 학반 정보
        Long teacherClass=mapper.teacherClass(teacher.getTeaId());
        if(teacherClass==null){
            throw new CustomException(HAS_NOT_CLASS);
        }
        haesolOnline.setClassId(teacherClass); //선생님 정보 토대로 학년 정보
        haesolOnline.setSubjectCode(subject); //과목코드
        haesolOnline.setTypeTag(typeTag); //객관식-주관식
        log.info("typeTag is null? {}", haesolOnline.getTypeTag());
        haesolOnline.setLevel(p.getLevel()); //난이도
        haesolOnline.setQuestion(p.getQuestion()); //문제
        haesolOnline.setContents(p.getContents()); //내용
        haesolOnline.setAnswer(p.getAnswer()+1); //보기 및 단답형 정답
        haesolOnline.setQueTag(p.getQueTag()); //객관식 주관식 구분
        haesolOnline.setPic(picName); // 사진 저장
        //haesolOnline.setCreatedAt(LocalDateTime.now()); //생성일자(상속)
        haesolOnline.setExplanation(p.getExplanation()); //해설
        // 문제 Entity에 저장
        log.info("OnlineEntity(haesolOnline) : {}", haesolOnline);
        haesolOnlineRepository.save(haesolOnline);

        // 사진을 폴더에 저장
        if (pic != null || !pic.isEmpty()) {
            try {
                String path = String.format("onlineKorMat/%s", haesolOnline.getQueId());
                customFileUtils.makeFolders(path);
                String target = String.format("%s/%s", path, picName);
                customFileUtils.transferTo(pic, target);
            } catch (Exception e) {
                e.printStackTrace();
                throw new CustomException(CAN_T_UPLOAD_QUESTION);
            }
        }

        //국어는 무조건 객관식, 수학은 섞임
        /*
        국어는 무조건 돌아야됨 / 해당 과정을 할지 안 할지 판단하는 건 수학뿐
        수학과목이고, 유형이 2이면(주관식이면) 안 돌아도 됨
        수학+주관이면 돌아라-> 수학+주관이 아니면 돌아라
        * */

        // 문제 유형이 객관식이라면 보기를 Entity에 저장
        //if (p.getQueTag() == 1) { //객관식이면 반복
        if(!(p.getSubjectCode()==2 && p.getQueTag()==2)){
            List<String> haesolOnlineMultipleList = p.getMultiple();
            log.info("multiple req : {}", p.getMultiple());
            int num = 1;
            List<HaesolOnlineMultiple> multipleList = new ArrayList<>();
            for (String mul : haesolOnlineMultipleList) {
                HaesolOnlineMultiple entHaesolOnlineMultiple = new HaesolOnlineMultiple();
                entHaesolOnlineMultiple.setHaesolOnline(haesolOnline);
                entHaesolOnlineMultiple.setNum(num);
                log.info("num:{} : string : {}", num, mul);
                entHaesolOnlineMultiple.setSentence(mul);
                num++;
                multipleList.add(entHaesolOnlineMultiple);
                log.info("OnlineEntity(entHaesolOnlineMultiple) : {}", entHaesolOnlineMultiple);
            }
            haesolOnlineMultipleRepository.saveAll(multipleList);
        }
        return 1;
    }

    //========= 시험 문제 리턴 =========
    public List<GetKoreanAndMathQuestionRes> GetKorAMatQuestion(GetKoreanAndMathQuestionReq p) {
        //학부모-> 자녀 학급, 학생->본인, 학급 선생님-> 담당학급 을 추출하는 과정이 필요
        //로그인 한 유저가 누구이며 그에 따른 학년 조회
        log.info("student PK {}", p.getStudentPk());
        MyUser user = authenticationFacade.getLoginUser(); // ROLE를 구분해서 관련 학년정보 리턴
        Long grade = methodStorage.signedUserGrade(user, p.getStudentPk());
        if (grade == null) {
            throw new CustomException(CAN_T_GET_GRADE);
        }
        log.info("서비스 사람 학년 정보 : {}", grade);
        //TypeTag typeTag = typeTagRepository.findByTypeNumAndSubject_SubjectId(p.getTypeTag(), p.getSubjectCode());

        // 학년과 과목 코드를 넣어서 전체 문제 리스트 조회 ex. 1학년 수학과목
        List<HaesolOnline> listAll = haesolOnlineRepository.findBySubjectCodeAndClassId(p.getSubjectCode(), grade);
        List<GetKoreanAndMathQuestionRes> list = new ArrayList<>(); //20문제만 뽑아 낼 새로운 리스트
        // 문제가 없을 때의 exception 처리
        if (listAll == null || listAll.isEmpty()) {
            throw new CustomException(NOT_FOUND_QUESTION);
        }
        Collections.shuffle(listAll);
        log.info("랜덤으로 문제를 섞음");

        for (int i = 0; i < (TOTAL_TEST_QUESTION < listAll.size() ? TOTAL_TEST_QUESTION : listAll.size()); i++) {
            GetKoreanAndMathQuestionRes a = new GetKoreanAndMathQuestionRes();
            //하나씩 문제를 객체에 저장
            a.setQuestion(listAll.get(i).getQuestion()); //문제
            a.setQueId(listAll.get(i).getQueId()); //PK
            a.setLevel(listAll.get(i).getLevel()); //난이도
            log.info("get res 태그 전: {}", a);
            //a.setTypeTag(listAll.get(i).getTypeTag().getTypeNum()); //
            //log.info("get Tag 아마 여기가 2개라서 : {}", a.getTypeTag());
            log.info("get res 태그 후: {}", a);
            a.setQueTag(listAll.get(i).getQueTag());
            a.setContents(listAll.get(i).getContents());
            //a.setAnswer(listAll.get(i).getAnswer());
            a.setPic(listAll.get(i).getPic());
            // 보기를 저장
            if (!(p.getSubjectCode() == 2 && listAll.get(i).getQueTag()==2)) { //NOT(과목이 수학이고 문제유형이 주관식이면)
                List<HaesolOnlineMultiple> aaa = haesolOnlineMultipleRepository.findSentenceByQueIdOrderByNum(listAll.get(i).getQueId());
                for (HaesolOnlineMultiple item : aaa) {
                    a.getSentence().add(item.getSentence());
                }
                log.info("리스트 add 직전");
            }
            list.add(a);
        }
            log.info("리턴 전");
            return list;
        }


    //========= 시험 문제를 풀고 그에 따른 점수(?) =========
    public TestOutCome testMarking(StudentOmr p) { //필요한 것 : 현재 출력된 문제들의 PK값 + 학생이 체크한 정답 리스트
        //문제 번호와 정답만 담은 리스트
        // ========================== 오답노트 ==========================
        StudentOnlineRecode onlineRecode=new StudentOnlineRecode();
        onlineRecode.setStudent(studentRepository.getReferenceById(authenticationFacade.getLoginUserId()));
        Subject subject=subjectRepository.getReferenceById(p.getSubjectCode());
        onlineRecode.setSubject(subject);
        onlineRecode.setTestTitle(p.getTitle());
        studentOnlineRecodeRepository.save(onlineRecode);

        if(p.getOmrAnswer().size()!=p.getQuestionPk().size()){
            throw new CustomException(OMR_ISN_T_CORRECT);
        }

        TestOutCome outCome=new TestOutCome(); //최종적으로 리턴할 객체
        StudentOmr testOutComeList=p; //TestOutCome에 담겨 틀린 문제의 리스트를 리턴(전체 문제의 PK, 학생답, 실제 답)
        outCome.setStudentOmr(testOutComeList);
        log.info("parameter P: {}", p);
        log.info("pklist: {}", p.getQuestionPk());
        List<Integer> realAnswer=new ArrayList<>();
        for(int i=0; i<p.getOmrAnswer().size();i++){
            Integer real=haesolOnlineRepository.findAnswerByQueId(p.getQuestionPk().get(i));
            if(real==null){
                throw new CustomException(EXCEED_PK_VALUE);
            }
            realAnswer.add(real);
        }

        outCome.setRealAnswer(realAnswer);
        log.info("P.setRealAnswer: {}", p);
        log.info("realAnswer : {}", realAnswer);
        List<String> typeString=new ArrayList<>();
        for(Long i:p.getQuestionPk()){
            String type=haesolOnlineRepository.findTypeNameByQueId(i);
            typeString.add(type);
            log.info("typeString {},{}", typeString.size(), type);
        }
        outCome.setTypeString(typeString);
            log.info("p.getTypeString {}",outCome.getTypeString());
        log.info("outCome : {}",outCome);

        for(int i=0; i<p.getQuestionPk().size(); i++){
            StudentOnlineTest studentOnlineTest=new StudentOnlineTest();
            studentOnlineTest.setStudentOnlineRecode(onlineRecode);
            studentOnlineTest.setHaesolOnline(haesolOnlineRepository.getReferenceById(p.getQuestionPk().get(i)));
            studentOnlineTest.setStuAnswer(testOutComeList.getOmrAnswer().get(i));
            studentOnlineTestRepository.save(studentOnlineTest);
        }

        return outCome;
        }

        public List<OnlineTestRecordListRes> testRecode(){ //오답노트 목록을 가져옴(학생 PK에 따라)
            Student student=studentRepository.getReferenceById(authenticationFacade.getLoginUserId());
            //이 학생 한 명에서 조회되는 값
            List<StudentOnlineRecode> testRecodes=studentOnlineRecodeRepository.getByStuOnId(student.getStuId());
            List<OnlineTestRecordListRes> resList=new ArrayList<>();
            for(StudentOnlineRecode a: testRecodes){
                OnlineTestRecordListRes res=new OnlineTestRecordListRes();
                res.setRecodePk(a.getStuOnId());
                res.setTitle(a.getTestTitle());
                res.setCreatedAt(a.getCreatedAt().toString());
                res.setSubject((a.getSubject().getSubjectId()==1?"국어":"수학"));
                resList.add(res);
            }
            return resList;
        }

        public List<TestOutCome> testQuestion(long recodePk){
        //여기
            return null;
        }

        public int tmpDeleteQuestion(@PathVariable long questionPk){
        int answer=mapper.tmpDeleteQuestionMul(questionPk);
        int result=mapper.tmpDeleteQuestion(questionPk);
        return answer+result;
        }

        public List<GetKoreanAndMathQuestionRes> tmpGetQuestion(@RequestBody long grade){
            List<HaesolOnline> listAll = haesolOnlineRepository.findByClassId(grade);
            List<GetKoreanAndMathQuestionRes> list = new ArrayList<>(); //20문제만 뽑아 낼 새로운 리스트
            // 문제가 없을 때의 exception 처리
            if (listAll == null || listAll.isEmpty()) {
                throw new CustomException(NOT_FOUND_QUESTION);
            }

            for (int i = 0; i < (TOTAL_TEST_QUESTION < listAll.size() ? TOTAL_TEST_QUESTION : listAll.size()); i++) {
                GetKoreanAndMathQuestionRes a = new GetKoreanAndMathQuestionRes();
                //하나씩 문제를 객체에 저장
                a.setQuestion(listAll.get(i).getQuestion()); //문제
                a.setQueId(listAll.get(i).getQueId()); //PK
                a.setLevel(listAll.get(i).getLevel()); //난이도
                log.info("get res 태그 전: {}", a);
                //a.setTypeTag(listAll.get(i).getTypeTag().getTypeNum()); //
                //log.info("get Tag 아마 여기가 2개라서 : {}", a.getTypeTag());
                log.info("get res 태그 후: {}", a);
                a.setQueTag(listAll.get(i).getQueTag());
                a.setContents(listAll.get(i).getContents());
                //a.setAnswer(listAll.get(i).getAnswer());
                a.setPic(listAll.get(i).getPic());
                // 보기를 저장
                if (listAll.get(i).getQueTag()!=2) { //NOT(과목이 수학이고 문제유형이 주관식이면)
                    List<HaesolOnlineMultiple> aaa = haesolOnlineMultipleRepository.findSentenceByQueIdOrderByNum(listAll.get(i).getQueId());
                    for (HaesolOnlineMultiple item : aaa) {
                        a.getSentence().add(item.getSentence());
                    }
                    log.info("리스트 add 직전");
                }
                list.add(a);
            }
            log.info("리턴 전");
            return list;
        }
}
