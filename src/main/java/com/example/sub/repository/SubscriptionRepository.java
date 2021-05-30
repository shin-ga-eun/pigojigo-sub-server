package com.example.sub.repository;

import com.example.sub.domain.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Subscription findBySid(Long sid);

    List<Subscription> findByApplcntEmail(String email);

    @Query("select count(*) from Subscription where applcnt_email= :email")
    int getSubCntByEmail(@Param("email") String email);
}
