package com.green.fefu.student.model.req;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.green.fefu.teacher.model.dataset.ExceptionMsgDataSet.*;

@Getter
@Setter
@ToString
public class updateStudentReq {
    @Pattern(regexp = "^[가-힣a-zA-Z\\s-]+$", message = NAME_PATTERN_ERROR)
    @Schema(example = "홍길동", description = "학생 이름")
    private String studentName;

    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = PHONE_PATTERN_ERROR)
    @Schema(example = "010-0000-0000", description = "전화번호")
    private String studentPhone;


    @Schema(example = "서울 판교로 112", description = "주소 + 상세주소")
    private String studentAddr;
    @Schema(example = "1234", description = "우편번호")
    private String studentZoneCode;
    @Schema(example = "101동",description = "상세 주소")
    private String studentDetail;


    @Schema(example = "갑각류 알러지 있음", description = "특이사항 기입")
    private String studentEtc;

//    @NotBlank(message = "Pk값은 필수입니다.")
    @Schema(example = "1", description = "바꿀 학생의 pk값", required = true)
    @Min(value = 1,message = "PK값은 필수 이며 1보다 커야 합니다.")
    private Long studentPk;

    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$", message = BIRTH_TYPE_ERROR)
    @Schema(example = "1999-01-01", description = "학생 생년월일")
    private String studentBirth;

    @JsonIgnore
    private String fullAddr;
    @JsonIgnore
    private String pic;

//    public void setFullAddr() {
//        if(studentAddr != null) {
//            this.fullAddr = studentZoneCode + "#" + studentAddr + "#" + studentDetail;
//        }
//    }
}
