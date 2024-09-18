package com.bussiness.advail_capitals.dtos;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChapterRequestDto {
    private String title;
    private String totalTime;
    private String totalVideo;
    private MultipartFile chapterImage;
}
