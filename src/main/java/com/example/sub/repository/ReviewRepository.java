package com.example.sub.repository;

import com.example.sub.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Review findByRid(Long rid);

    List<Review> findAll();

    List<Review> findTop5ByOrderByRidDesc();
}
