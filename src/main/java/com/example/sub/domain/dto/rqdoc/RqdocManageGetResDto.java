package com.example.sub.domain.dto.rqdoc;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RqdocManageGetResDto {

    private String applcntEmail;

    private Long sid;

    private String rqdocSn;
    private String reqDtm;

    private String currentPs;
    private String actionPs;

    private String sizeCd;
    private String pickUpCycleCd;
    private String paymentCycleCd;
    private String paymentMthCd;

    private int price; // 총 금액
    private int remainPrice; // 잔여금

    private int totalCnt;
    private int remainCnt;

    public void setPaymentMthCd(String value) {

        if ("PmMthCd1".equals(value)) {
            this.paymentMthCd = "무통장입금";
        }
    }

}
