package com.example.sub.service.rqdoc;

import com.example.sub.domain.dto.rqdoc.*;
import com.example.sub.domain.entity.RqdocHst;
import com.example.sub.domain.entity.Subscription;
import com.example.sub.repository.RqdocHstRepository;
import com.example.sub.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RqdocService {

    final private SubscriptionRepository subscriptionRepository;
    final private RqdocHstRepository rqdocHstRepository;

    public RqdocService(RqdocHstRepository rqdocHstRepository, SubscriptionRepository subscriptionRepository) {
        this.rqdocHstRepository = rqdocHstRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @Transactional
    public void apply(RqdocApplyDto applyDto) {

        String rqdocSn = makeRqdocSn();

        RqdocHst rqdocHst = new RqdocHst();
        rqdocHst.setProgsCd("신청"); //신청
        rqdocHst.setRqdocSn(rqdocSn);

        Subscription rqdoc = new Subscription();
        rqdoc.setSizeCd(applyDto.getSizeCd());
        rqdoc.setPaymentCycleCd(applyDto.getPaymentCycleCd());
        rqdoc.setPickUpCycleCd(applyDto.getPickUpCycleCd());
        rqdoc.setPaymentMthCd(applyDto.getPaymentMthCd());
        rqdoc.setVaseYn(applyDto.getVaseYn());
        rqdoc.setApplcntEmail(applyDto.getApplcntEmail());
        rqdoc.setPrice(makeInitPrice(applyDto));

        Date reqDtm = new Date();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");

        rqdoc.setReqDtm(transFormat.format(reqDtm));

        Subscription sub = subscriptionRepository.save(rqdoc);
        rqdocHst.setSubscription(sub);

        rqdocHstRepository.save(rqdocHst);
    }

    public List<RqdocGetDto> getListByEmail(String email) {

        List<Subscription> subs = subscriptionRepository.findByApplcntEmail(email);
        List<RqdocGetDto> list = new ArrayList<>();

        for (Subscription sub : subs) {

            RqdocGetDto dto = new RqdocGetDto();

            RqdocHst rqdocHst = rqdocHstRepository.findBySubscription(sub);
            dto.setRqdocSn(rqdocHst.getRqdocSn());
            dto.setPaymentCycleCd(sub.getPaymentCycleCd());
            dto.setPaymentMthCd(sub.getPaymentMthCd());
            dto.setPickUpCycleCd(sub.getPickUpCycleCd());
            dto.setSizeCd(sub.getSizeCd());
            dto.setVaseYn(sub.getVaseYn());
            dto.setReqDtm(sub.getReqDtm());
            dto.setSid(sub.getSid());
            dto.setPrice(sub.getPrice());

            int totalCnt = calTotalCnt(sub);
            int curCnt = rqdocHst.getCnt();
            int remainCnt = totalCnt - curCnt;

            dto.setTotalCnt(totalCnt);
            dto.setRemainCnt(remainCnt);
            dto.setRemainPrice(calPrice(sub, curCnt));
            dto.setOncePrice(sub.getPrice() / totalCnt);
            dto.setProgrsCd(rqdocHst.getProgsCd());

            list.add(dto);
        }

        return list;
    }

    @Transactional
    public void pay(PayDto payDto) {

        Subscription param = subscriptionRepository.findBySid(payDto.getSid());

        RqdocHst rqdocHst = rqdocHstRepository.findBySubscription(param);
        int calCnt = rqdocHst.getCnt() + 1;
        rqdocHst.setCnt(calCnt);

        int total = Integer.parseInt(param.getPickUpCycleCd()) * Integer.parseInt(param.getPaymentCycleCd());
        rqdocHst.setProgsCd("결제완료");

        rqdocHstRepository.save(rqdocHst);
    }

    private int calPrice(Subscription subscription, int curCnt) {

        int totalPrice = subscription.getPrice();
        int totalCnt = calTotalCnt(subscription);
        int cal = 0;

        cal = totalPrice - (totalPrice / totalCnt) * (curCnt + 1);

        return cal;
    }

    private int calMngPrice(Subscription subscription, int curCnt) {

        int totalPrice = subscription.getPrice();
        int totalCnt = calTotalCnt(subscription);
        int cal = 0;

        cal = totalPrice - (totalPrice / totalCnt) * (curCnt);

        return cal;
    }

    private int calTotalCnt(Subscription subscription) {
        return Integer.parseInt(subscription.getPickUpCycleCd()) * Integer.parseInt(subscription.getPaymentCycleCd());
    }

    private int makeInitPrice(RqdocApplyDto rqdocApplyDto) {

        int price = 0;

        if ("S".equals(rqdocApplyDto.getSizeCd())) {
            price += 10000;
        } else {
            price += 20000;
        }

        // 수령 주기
        price *= Integer.parseInt(rqdocApplyDto.getPickUpCycleCd());

        // 수령 기간
        price *= Integer.parseInt(rqdocApplyDto.getPaymentCycleCd());

        if ("Y".equals(rqdocApplyDto.getVaseYn())) {
            price += 5000;
        }

        return price;
    }

    private String makeRqdocSn() {

        StringBuilder rqdocSn = new StringBuilder();

        Date reqDtm = new Date();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
        String s = transFormat.format(reqDtm).replace("-", "");

        rqdocSn.append(s);
        rqdocSn.append("-");
        rqdocSn.append(rqdocHstRepository.getLastIndex() + 1);

        return rqdocSn.toString();
    }

    public void updateProgrsCd(RqdocProgrsDto rqdocProgrsDto) {

        Subscription param = subscriptionRepository.findBySid(rqdocProgrsDto.getSid());
        RqdocHst rqdocHst = rqdocHstRepository.findBySubscription(param);

        rqdocHst.setProgsCd(rqdocProgrsDto.getActionPs());

        rqdocHstRepository.save(rqdocHst);
    }

    //결제완료상태 신청서 조회
    public List<RqdocManageGetResDto> getRqdocsByProgsCd (RqdocManageGetReqDto rqdocManageGetReqDto) {

        List<RqdocManageGetResDto> resDtos = new ArrayList<>();
        List<RqdocHst> rqdocHsts = rqdocHstRepository.findByProgsCd(rqdocManageGetReqDto.getCurrentPs());

        for (RqdocHst rqdocHst : rqdocHsts) {

            RqdocManageGetResDto resDto = new RqdocManageGetResDto();

            Subscription sub = rqdocHst.getSubscription();

            resDto.setActionPs(rqdocManageGetReqDto.getActionPs());
            resDto.setApplcntEmail(sub.getApplcntEmail());
            resDto.setSid(sub.getSid());
            resDto.setRqdocSn(rqdocHst.getRqdocSn());
            resDto.setReqDtm(sub.getReqDtm());
            resDto.setCurrentPs(rqdocManageGetReqDto.getCurrentPs());
            resDto.setSizeCd(sub.getSizeCd());
            resDto.setPickUpCycleCd(sub.getPickUpCycleCd());
            resDto.setPaymentCycleCd(sub.getPaymentCycleCd());
            resDto.setPaymentMthCd(sub.getPaymentMthCd());
            resDto.setPrice(sub.getPrice());
            resDto.setRemainPrice(calMngPrice(sub, rqdocHst.getCnt()));
            resDto.setTotalCnt(calTotalCnt(sub));

            int remainCnt = calTotalCnt(sub) - rqdocHst.getCnt();
            resDto.setRemainCnt(remainCnt);

            resDtos.add(resDto);
        }

        return resDtos;
    }

    public RqdocCountsDto getCounts (){

        RqdocCountsDto dto = new RqdocCountsDto();

        int cnt1 = rqdocHstRepository.getCountByProgsCd("결제완료");
        int cnt2 = rqdocHstRepository.getCountByProgsCd("배송대기");
        int cnt3 = rqdocHstRepository.getCountByProgsCd("배송중");
        int cnt4 = rqdocHstRepository.getCountByProgsCd("배송완료");
        int cnt5 = rqdocHstRepository.getCountByProgsCd("구독종료");

        dto.setCnt1(cnt1);
        dto.setCnt2(cnt2);
        dto.setCnt3(cnt3);
        dto.setCnt4(cnt4);
        dto.setCnt5(cnt5);

        return dto;
    }



}
