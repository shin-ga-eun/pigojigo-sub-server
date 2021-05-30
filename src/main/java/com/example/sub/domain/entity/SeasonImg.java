package com.example.sub.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class SeasonImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seasonImageNo;

    private String imageName;
    private String imageType;
    private String imagePath;
    private Long imageSize;
    private String imageUrl;

    @ManyToOne(targetEntity = Season.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "sid")
    private Season season;
}
