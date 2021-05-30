package com.example.sub.domain.dto.rqdoc;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RqdocCountsDto {

    private int cnt1; //결제완료
    private int cnt2; //배송 대기
    private int cnt3; //배송 중
    private int cnt4; //배송 완료
    private int cnt5; //구독 종료
}
