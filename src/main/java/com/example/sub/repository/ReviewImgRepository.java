package com.example.sub.repository;

import com.example.sub.domain.entity.Review;
import com.example.sub.domain.entity.ReviewImg;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface ReviewImgRepository extends JpaRepository<ReviewImg, Long>{

    Optional<ReviewImg> findByReview(Review card);

}
