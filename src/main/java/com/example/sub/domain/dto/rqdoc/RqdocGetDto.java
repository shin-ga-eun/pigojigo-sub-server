package com.example.sub.domain.dto.rqdoc;

import com.example.sub.domain.entity.Subscription;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RqdocGetDto {

    private Long sid;

    private String rqdocSn; // 20200101-001 형
    private String sizeCd;
    private String pickUpCycleCd;
    private String paymentCycleCd;
    private String paymentMthCd;
    private String vaseYn;

    private int price; // 총 금액
    private int remainPrice; // 잔여금
    private int oncePrice; // 한번 졀제 금액

    private String reqDtm;
    private String progrsCd;

    private int totalCnt;
    private int remainCnt;

    public void setVaseYn(String value) {

        if ("Y".equals(value)) {
            this.vaseYn = "신청함";
        } else {
            this.vaseYn = "신청하지 않음";
        }
    }

    public void setSizeCd(String value) {

        if ("S".equals(value)) {
            this.sizeCd = "S (Small)";
        } else if ("M".equals(value)) {
            this.sizeCd = "M (Medium)";
        }
    }

    public void setPaymentMthCd(String value) {

        if ("PmMthCd1".equals(value)) {
            this.paymentMthCd = "무통장입금";
        }
    }

    public void setPaymentCycleCd(String value) {

        this.paymentCycleCd = value + "개월동안 구독";
    }

    public void setPickUpCycleCd(String value) {

        if ("1".equals(value)) {
            this.pickUpCycleCd = "한달에 1번 수령 (Received once a month) ";
        } else if ("2".equals(value)) {
            this.pickUpCycleCd = "2주에 한번 수령 (Received once every 2 weeks) ";
        }
    }
}
