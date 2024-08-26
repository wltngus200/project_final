package com.green.fefu.online;

import com.green.fefu.entity.dummy.Subject;
import com.green.fefu.entity.dummy.TypeTag;
import com.green.fefu.online.model.AddTagInfoReq;
import com.green.fefu.online.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeachersTaskService {
    private final SubjectRepository subjectRepository;
    public void addTypeTag(AddTagInfoReq p){
        TypeTag tag=new TypeTag();
        tag.setSubject(subjectRepository.getReferenceById(p.getSubject()));
        tag.setTypeNum(p.getTypeNum());
        tag.setTagName(p.getTagName());
    }

}
