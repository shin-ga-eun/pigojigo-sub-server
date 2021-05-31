package com.example.sub.service.season;

import com.example.sub.domain.dto.review.ReviewGetDto;
import com.example.sub.domain.dto.review.ReviewSaveDto;
import com.example.sub.domain.dto.season.SeasonDeleteDto;
import com.example.sub.domain.dto.season.SeasonGetDto;
import com.example.sub.domain.dto.season.SeasonSaveDto;
import com.example.sub.domain.entity.*;
import com.example.sub.repository.SeasonImgRepository;
import com.example.sub.repository.SeasonRepository;
import org.apache.commons.io.IOUtils;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sun.awt.im.InputMethodAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SeasonService {

    private static final String IMAGE_PATH = "resources.file.path";
    private static final String IMAGE_URL = "resources.file.url";

    final private SeasonRepository seasonRepository;
    final private SeasonImgRepository seasonImgRepository;

    private Environment environment;

    public SeasonService(SeasonRepository seasonRepository, SeasonImgRepository seasonImgRepository, Environment environment){
        this.seasonImgRepository = seasonImgRepository;
        this.seasonRepository = seasonRepository;
        this.environment = environment;
    }

    @Transactional
    public Season saveSeasonAndFile(SeasonSaveDto seasonSaveDto, MultipartFile file) throws IOException {

        Season season = new Season();

        season.setFlowerInfo(seasonSaveDto.getFlowerInfo());
        season.setFlowerNm(seasonSaveDto.getFlowerNm());
        season.setMonth(seasonSaveDto.getMonth());

        Season param = seasonRepository.save(season);
        SeasonImg img = saveImg(file, param);
        seasonImgRepository.save(img);

        return season;
    }

    public List<SeasonGetDto> getSeasons() {

        List<SeasonGetDto> dtos = new ArrayList<>();
        List<Season> list = seasonRepository.findAll();

        return getSeasonGetDtos(dtos, list);
    }

    public List<SeasonGetDto> getSeasonsByMonth() {

        List<SeasonGetDto> dtos = new ArrayList<>();
        List<Season> list = seasonRepository.findByMonth(5);

        return getSeasonGetDtos(dtos, list);
    }

    public void delSeason(SeasonDeleteDto seasonDeleteDto) {
        Season season = seasonRepository.findBySid(seasonDeleteDto.getSid());
        SeasonImg seasonImg = seasonImgRepository.findBySeason(season).get();

        seasonImgRepository.delete(seasonImg);
        seasonRepository.delete(season);
    }

    public SeasonImg saveImg(MultipartFile file, Season season) throws IOException {

        SeasonImg img = new SeasonImg();

        UUID uid = UUID.randomUUID();
        String fileName = uid + "_" + file.getOriginalFilename();
        String savePath = makePath(environment.getProperty(IMAGE_PATH));
        File destinationFile = new File(environment.getProperty(IMAGE_PATH) + savePath, fileName);

        file.transferTo(destinationFile);

        String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/season/image/" + season.getSid())
                .toUriString();

        img.setImageName(file.getOriginalFilename());
        img.setImageSize(file.getSize());
        img.setImageType(file.getContentType());
        img.setImageUrl(imageUrl);
        img.setImagePath(environment.getProperty(IMAGE_PATH) + savePath + File.separator + fileName);
        img.setSeason(season);

        return img;
    }

    public byte[] getImageResource(Long sid) {

        SeasonImg cardImage = seasonImgRepository.findBySeason(seasonRepository.findBySid(sid)).get();

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

    private List<SeasonGetDto> getSeasonGetDtos(List<SeasonGetDto> dtos, List<Season> list) {
        for (Season season : list) {

            SeasonGetDto dto = new SeasonGetDto();

            dto.setFlowerInfo(season.getFlowerInfo());
            dto.setFlowerNm(season.getFlowerNm());
            dto.setMonth(season.getMonth());
            dto.setImgUrl(environment.getProperty(IMAGE_URL)+"/season/image/"+season.getSid());
            dto.setSid(season.getSid());

            dtos.add(dto);
        }

        return dtos;
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
}
