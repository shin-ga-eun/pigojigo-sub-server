package com.example.sub.controller;

import com.example.sub.domain.dto.review.ReviewGetDto;
import com.example.sub.domain.dto.review.ReviewSaveDto;
import com.example.sub.domain.entity.Review;
import com.example.sub.service.review.ReviewService;
import com.example.sub.service.rqdoc.RqdocService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class ReviewController {

    final private ReviewService reviewService;
    private ObjectMapper objectMapper;

    public ReviewController(ReviewService reviewService, ObjectMapper objectMapper) {
        this.reviewService = reviewService;
        this.objectMapper = objectMapper;
    }

    @RequestMapping(value = "/review/save", method = RequestMethod.POST)
    public void save(@RequestPart MultipartFile file, @RequestParam("json") String json) throws Exception {

        ReviewSaveDto saveDto = objectMapper.readValue(json, ReviewSaveDto.class);
        reviewService.saveReviewAndFile(saveDto, file);
    }

    @RequestMapping(value = "/main/reviews", method = RequestMethod.GET)
    public List<ReviewGetDto> getTop5Reviews() {

        return reviewService.getTop5Reviews();
    }

    @RequestMapping(value = "/review/image/{rid}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable Long rid) {

        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                .body(reviewService.getImageResource(rid));
    }

    @RequestMapping(value = "/reviews", method = RequestMethod.GET)
    public List<ReviewGetDto> getReviews() {

        return reviewService.getReviews();
    }
}
