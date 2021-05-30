package com.example.sub.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
public class RqdocHst {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rid;


    private String rqdocSn;
    private String progsCd;

    @OneToOne(targetEntity = Subscription.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "sid")
    private Subscription subscription;

    private int cnt; //임시컬럼
}
