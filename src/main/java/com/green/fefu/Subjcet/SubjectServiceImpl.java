package com.green.fefu.Subjcet;

import com.green.fefu.Subjcet.model.SubjectReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubjectServiceImpl implements SubjectService {
    private final SubjectMapper mapper;
    // 성적 입력칸
    @Override
    public long postSubject(SubjectReq p) {
        return mapper.SubjectReq(p);
    }
}
