package com.example.sub.domain.dto.review;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReviewGetDto {

    private String reviewCn;
    private String singleLineEval;

    private String reqDtm; //리뷰작성일

    private String nickname;
    private String birth;
    private String sex;
    private int subCnt;

    private String imgUrl;

}
