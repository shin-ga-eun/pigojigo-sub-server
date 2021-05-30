package com.example.sub.repository;

import com.example.sub.domain.entity.Review;
import com.example.sub.domain.entity.ReviewImg;
import com.example.sub.domain.entity.Season;
import com.example.sub.domain.entity.SeasonImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SeasonImgRepository extends JpaRepository<SeasonImg, Long> {

    Optional<SeasonImg> findBySeason(Season card);
}
