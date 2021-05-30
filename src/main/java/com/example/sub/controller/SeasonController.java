package com.example.sub.controller;

import com.example.sub.domain.dto.review.ReviewGetDto;
import com.example.sub.domain.dto.review.ReviewSaveDto;
import com.example.sub.domain.dto.season.SeasonDeleteDto;
import com.example.sub.domain.dto.season.SeasonGetDto;
import com.example.sub.domain.dto.season.SeasonSaveDto;
import com.example.sub.service.season.SeasonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class SeasonController {

    final private SeasonService seasonService;
    private ObjectMapper objectMapper;

    public SeasonController(SeasonService seasonService, ObjectMapper objectMapper){
        this.seasonService = seasonService;
        this.objectMapper = objectMapper;
    }

    @RequestMapping(value = "/season/save", method = RequestMethod.POST)
    public void save(@RequestPart MultipartFile file, @RequestParam("json") String json) throws Exception {

        SeasonSaveDto saveDto = objectMapper.readValue(json, SeasonSaveDto.class);
        seasonService.saveSeasonAndFile(saveDto, file);
    }

    @RequestMapping(value = "/season/image/{sid}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable Long sid) {

        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                .body(seasonService.getImageResource(sid));
    }

    @RequestMapping(value = "/seasons", method = RequestMethod.GET)
    public List<SeasonGetDto> getSeasons() {

        return seasonService.getSeasons();
    }

    @RequestMapping(value="/season/del", method=RequestMethod.POST)
    public void delSeason (@RequestBody SeasonDeleteDto seasonDeleteDto){
        seasonService.delSeason(seasonDeleteDto);
    }
}
