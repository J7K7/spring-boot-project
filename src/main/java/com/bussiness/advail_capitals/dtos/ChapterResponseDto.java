package com.bussiness.advail_capitals.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChapterResponseDto {
    private Integer id;
    private String title;
    private String chapterImage;
    private String totalTime;
    private String totalVideo;
}
