package com.example.sub.service.review;

import com.example.sub.domain.dto.review.ReviewGetDto;
import com.example.sub.domain.dto.review.ReviewSaveDto;
import com.example.sub.domain.entity.Review;
import com.example.sub.domain.entity.ReviewImg;
import com.example.sub.domain.entity.User;
import com.example.sub.repository.*;
import org.apache.commons.io.IOUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.spi.CalendarDataProvider;

@Service
public class ReviewService {

    private static final String IMAGE_PATH = "resources.file.path";
    private static final String IMAGE_URL = "resources.file.url";


    final private ReviewRepository reviewRepository;
    final private ReviewImgRepository reviewImgRepository;
    final private UserRepository userRepository;
    final private RqdocHstRepository rqdocHstRepository;
    final private SubscriptionRepository subscriptionRepository;

    private Environment environment;

    public ReviewService(ReviewRepository reviewRepository, ReviewImgRepository reviewImgRepository, UserRepository userRepository, RqdocHstRepository rqdocHstRepository, Environment environment, SubscriptionRepository subscriptionRepository) {
        this.reviewRepository = reviewRepository;
        this.reviewImgRepository = reviewImgRepository;
        this.userRepository = userRepository;
        this.rqdocHstRepository = rqdocHstRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.environment = environment;
    }


    @Transactional
    public Review saveReviewAndFile(ReviewSaveDto reviewSaveDto, MultipartFile file) throws IOException {
        Review review = new Review();

        review.setApplcntEmail(reviewSaveDto.getApplcntEmail());
        review.setSingleLineEval(reviewSaveDto.getSingleLineEval());
        review.setReviewCn(reviewSaveDto.getReviewCn());

        Date reqDtm = new Date();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
        review.setReqDtm(transFormat.format(reqDtm));

        Review param = reviewRepository.save(review);
        ReviewImg img = saveImg(file, param);
        reviewImgRepository.save(img);

        return review;
    }

    public ReviewImg saveImg(MultipartFile file, Review review) throws IOException {

        ReviewImg img = new ReviewImg();

        UUID uid = UUID.randomUUID();
        String fileName = uid + "_" + file.getOriginalFilename();
        String savePath = makePath(environment.getProperty(IMAGE_PATH));
        File destinationFile = new File(environment.getProperty(IMAGE_PATH) + savePath, fileName);

        file.transferTo(destinationFile);

        String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/review/image/" + review.getRid())
                .toUriString();

        img.setImageName(file.getOriginalFilename());
        img.setImageSize(file.getSize());
        img.setImageType(file.getContentType());
        img.setImageUrl(imageUrl);
        img.setImagePath(environment.getProperty(IMAGE_PATH) + savePath + File.separator + fileName);
        img.setReview(review);

        return img;
    }

    public List<ReviewGetDto> getTop5Reviews() {

        List<ReviewGetDto> dtos = new ArrayList<>();
        List<Review> list = reviewRepository.findTop5ByOrderByRidDesc();

        return getReviewGetDtos(dtos, list);
    }

    public List<ReviewGetDto> getReviews() {

        List<ReviewGetDto> dtos = new ArrayList<>();
        List<Review> list = reviewRepository.findAll();

        return getReviewGetDtos(dtos, list);
    }

    private List<ReviewGetDto> getReviewGetDtos(List<ReviewGetDto> dtos, List<Review> list) {
        for (Review review : list) {

            ReviewGetDto dto = new ReviewGetDto();

            User user = userRepository.findByEmail(review.getApplcntEmail());
            dto.setBirth(makeBirthToAgeRange(user.getBirth()));
            dto.setNickname(user.getNickname());
            dto.setSex(user.getSex());
            dto.setReqDtm(review.getReqDtm());
            dto.setReviewCn(review.getReviewCn());
            dto.setSingleLineEval(review.getSingleLineEval());
            dto.setSubCnt(subscriptionRepository.getSubCntByEmail(review.getApplcntEmail()));
            dto.setImgUrl(environment.getProperty(IMAGE_URL) + "/review/image/" + review.getRid());

            dtos.add(dto);
        }

        return dtos;
    }

    public byte[] getImageResource(Long rid) {

        ReviewImg cardImage = reviewImgRepository.findByReview(reviewRepository.findByRid(rid)).get();

        byte[] result = null;
        try {
            File file = new File(cardImage.getImagePath());

            InputStream in = new FileInputStream(file);
            result = IOUtils.toByteArray(in);

            return result;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String makePath(String uploadPath) {

        Calendar calendar = Calendar.getInstance();

        String yearPath = File.separator + calendar.get(Calendar.YEAR);
        String monthPath = yearPath + File.separator + new DecimalFormat("00").format(calendar.get(Calendar.MONTH) + 1);
        String datePath = monthPath + File.separator + new DecimalFormat("00").format(calendar.get(Calendar.DATE));

        makeDir(uploadPath, yearPath, monthPath, datePath);

        return datePath;
    }

    private void makeDir(String uploadPath, String... paths) {

        if (new File(uploadPath + paths[paths.length - 1]).exists()) {
            return;
        }

        for (String path : paths) {
            File dirPath = new File(uploadPath + path);

            if (!dirPath.exists()) {
                dirPath.mkdir();
            }
        }
    }

    private String makeBirthToAgeRange(String birth) {

        String ageRange = "";

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        int comp = year - Integer.parseInt(birth.substring(0, 4));

        if (comp / 10 == 0) {
            ageRange = "10대 미만";
        } else {
            ageRange = comp / 10 + "0대";
        }

        return ageRange;
    }

}
