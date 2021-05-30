package com.example.sub.domain.dto.season;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeasonGetDto {

    private String flowerNm;
    private String flowerInfo;

    private int month;
    private long sid;

    private String imgUrl;
}
