package com.example.sub.repository;

import com.example.sub.domain.entity.RqdocHst;
import com.example.sub.domain.entity.Subscription;
import com.example.sub.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RqdocHstRepository extends JpaRepository<RqdocHst, Long> {


    int getCntByRqdocSn(String rqdocSn);

    List<RqdocHst> findByProgsCd (String progsCd);

    @Query("select count(*) from RqdocHst")
    int getLastIndex();

    @Query("select count(*) from RqdocHst where progs_cd = :progsCd")
    int getCountByProgsCd(String progsCd);

    RqdocHst findBySubscription(Subscription subscription);
}
