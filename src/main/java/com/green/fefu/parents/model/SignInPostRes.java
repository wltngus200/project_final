package com.green.fefu.parents.model;

import com.green.fefu.entity.Student;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class SignInPostRes {
    @Schema(example = "1", description = "pk")
    private long parentsId;
    @Schema(example = "홍길동", description = "이름")
    private String nm;

    @Schema(description = "자녀 List")
    private List<StudentRes> studentList ;

    private String accessToken;
    @Schema(description = "필수값 여부 존재: 1, X: 2", defaultValue = "1")
    private int result ;
}
