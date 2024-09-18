package com.bussiness.advail_capitals.dtos;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopicRequestDto {
    private String topicTitle;
    private MultipartFile topicImage;
    private String topicVideoLink;
    private String topicDescription;
    private Integer chapterId;
    private List<MultipartFile> documents;
}
