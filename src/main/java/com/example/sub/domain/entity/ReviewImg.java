package com.example.sub.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class ReviewImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardImageNo;

    private String imageName;
    private String imageType;
    private String imagePath;
    private Long imageSize;
    private String imageUrl;

    @ManyToOne(targetEntity = Review.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "rid")
    private Review review;
}
