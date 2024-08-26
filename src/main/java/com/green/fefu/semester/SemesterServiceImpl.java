package com.green.fefu.semester;

import com.green.fefu.Subjcet.SubjectMapper;
import com.green.fefu.Subjcet.model.SubjectReq;
import com.green.fefu.semester.model.SemesterReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SemesterServiceImpl implements SemesterService {
    private final SemesterMapper mapper;
    // 성적 입력칸
    @Override
    public int postSemester(SemesterReq p) {
        return mapper.postSemester(p);
    }
}
