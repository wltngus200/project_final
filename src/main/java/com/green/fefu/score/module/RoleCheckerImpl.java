package com.green.fefu.score.module;

import com.green.fefu.entity.Student;
import com.green.fefu.exception.CustomException;
import com.green.fefu.score.ScoreMapper;
import com.green.fefu.score.model.GetClassIdRes;
import com.green.fefu.score.model.InsScoreReq;
import com.green.fefu.security.AuthenticationFacade;
import com.green.fefu.security.MyUser;
import com.green.fefu.student.model.dto.getDetail;
import com.green.fefu.student.service.StudentMapper;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import static com.green.fefu.exception.LJH.LjhErrorCode.SCORE_INSERT_POST;
import static com.green.fefu.exception.LJH.LjhErrorCode.SCORE_INSERT_STU_POST;

@Component
@RequiredArgsConstructor
public class RoleCheckerImpl implements Check {
    private final AuthenticationFacade authenticationFacade;

//    @Autowired
//    public RoleCheckerImpl(AuthenticationFacade authenticationFacade) {
//        this.authenticationFacade = authenticationFacade;
//    }

    @Override
    public void checkTeacherRole() {
        MyUser user = authenticationFacade.getLoginUser();
        String userRole = user.getRole();
        if (!userRole.equals("ROLE_TEACHER")) {
            throw new CustomException(SCORE_INSERT_POST);
        }
    }
}
