package com.example.sub.repository;

import com.example.sub.domain.entity.Review;
import com.example.sub.domain.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeasonRepository extends JpaRepository<Season, Long> {

    Season findBySid(Long sid);

    List<Season> findAll();

//    List<Season> findByMonth();
}
