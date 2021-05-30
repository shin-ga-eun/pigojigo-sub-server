package com.example.sub.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@Entity
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sid;

    private String sizeCd;
    private String pickUpCycleCd;
    private String paymentCycleCd;
    private String paymentMthCd;
    private String vaseYn;
    private String applcntEmail; //신청자이메일

    private int price;

    private String reqDtm;

}
