package com.green.fefu.chcommon;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

import static com.green.fefu.teacher.model.dataset.ExceptionMsgDataSet.*;

@Component
public class PatternCheck {
    private final String ID_PATTERN = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{6,12}$";
    private final Pattern idPattern = Pattern.compile(ID_PATTERN);
    private final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d|.*[!@#$%^&*()\\-_=+\\\\|\\[{\\]};:'\",<.>/?]).{8,20}$";
    private final Pattern passwordPattern = Pattern.compile(PASSWORD_PATTERN);
    private final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
    private final String NAME_PATTERN = "^[가-힣a-zA-Z\\s-]+$";
    private final Pattern namePattern = Pattern.compile(NAME_PATTERN);
    private final String PHONE_PATTERN = "^010-\\d{4}-\\d{4}$";
    private final Pattern phonePattern = Pattern.compile(PHONE_PATTERN);
    private final String GRADE_PATTERN = "^[1-6]\\d{4}$";
    private final Pattern gradePattern = Pattern.compile(GRADE_PATTERN);
    private final String GRADE_NUMBER_PATTERN = "^[1-6](0[1-9]|[1-9][0-9])(0[1-9]|[1-9][0-9])$";
    private final Pattern gradeNumberPattern = Pattern.compile(GRADE_NUMBER_PATTERN);
    public void idCheck(String p) {
        if (!idPattern.matcher(p).matches()) {
            throw new RuntimeException(ID_PATTERN_ERROR);
        }
    }

    public void emailCheck(String p) {
        if (!emailPattern.matcher(p).matches()) {
            throw new RuntimeException(EMAIL_PATTERN_ERROR);
        }
    }

    public void nameCheck(String p) {
        if (!namePattern.matcher(p).matches()) {
            throw new RuntimeException(NAME_PATTERN_ERROR);
        }
    }
    public void phoneCheck(String p) {
        if (!phonePattern.matcher(p).matches()) {
            throw new RuntimeException(PHONE_PATTERN_ERROR);
        }
    }
    public void passwordCheck(String p) {
        if (!passwordPattern.matcher(p).matches()) {
            throw new RuntimeException(PASSWORD_PATTERN_ERROR);
        }
    }

    public void gradeCheck(String p) {
        if (!gradePattern.matcher(p).matches()) {
            throw new RuntimeException(STUDENT_GRADE_DATA_ERROR);
        }
        int classNum = Integer.parseInt(p.substring(1, 3));
        int number = Integer.parseInt(p.substring(3, 5));


        if (classNum < 1) {
            throw new RuntimeException(STUDENT_GRADE_DATA_ERROR);
        }
        if (number < 1) {
            throw new RuntimeException(STUDENT_GRADE_DATA_ERROR);
        }
    }

    public void gradeNumberCheck(String p) {
        if (!gradeNumberPattern.matcher(p).matches()) {
            throw new RuntimeException(STUDENT_GRADE_DATA_ERROR);
        }
    }

}
