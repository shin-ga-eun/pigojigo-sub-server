package com.example.sub.domain.dto.review;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReviewSaveDto {

    private String reviewCn;
    private String singleLineEval;

    private String applcntEmail; //신청자이메일
}
