package com.example.sub.domain.dto.rqdoc;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class RqdocApplyDto {

    private String sizeCd;
    private String pickUpCycleCd;
    private String paymentCycleCd;
    private String paymentMthCd;
    private String vaseYn;
    private String applcntEmail; //신청자이메일

}
