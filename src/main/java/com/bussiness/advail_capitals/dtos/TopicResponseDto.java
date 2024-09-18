package com.bussiness.advail_capitals.dtos;

import java.util.Set;

import com.bussiness.advail_capitals.entities.Chapter;
import com.bussiness.advail_capitals.entities.TopicDocument;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopicResponseDto {
    private Integer Id;
    private String topicTitle;
    private String topicImageUrl;
    private String topicVideoLink;
    private String topicDescription;
    private Chapter chapter;
    private Set<TopicDocument> documents;
}
