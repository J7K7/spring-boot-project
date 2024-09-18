package com.bussiness.advail_capitals.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bussiness.advail_capitals.dtos.TopicRequestDto;
import com.bussiness.advail_capitals.dtos.TopicResponseDto;
import com.bussiness.advail_capitals.entities.Chapter;
import com.bussiness.advail_capitals.entities.Topic;
import com.bussiness.advail_capitals.entities.TopicDocument;
import com.bussiness.advail_capitals.repositories.ChapterRepository;
import com.bussiness.advail_capitals.repositories.TopicDocumentRepository;
import com.bussiness.advail_capitals.repositories.TopicRepository;

@Service
public class TopicService {
    private final Path rootLocation = Paths.get("upload");
    private static final String IMAGE_UPLOAD_DIR = "/images/";
    private static final String DOC_UPLOAD_DIR = "/documents";

    private static final Logger logger = LoggerFactory.getLogger(TopicService.class);

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private TopicDocumentRepository topicDocumentRepository;

    private TopicResponseDto mapToDto(Topic topic, Set<TopicDocument> topics){
        TopicResponseDto dto = new TopicResponseDto();
        dto.setId(topic.getId());
        dto.setChapter(topic.getChapter());
        dto.setDocuments(topics);
        dto.setTopicDescription(topic.getTopicDescription());
        dto.setTopicImageUrl(topic.getTopicImage());
        dto.setTopicTitle(topic.getTopicTitle());
        dto.setTopicVideoLink(topic.getTopicVideoLink());
        return dto;
    }

    @Transactional
    public TopicResponseDto CreateTopic(TopicRequestDto topicRequestDto) throws IOException{
        try {
            logger.info("Save Chapter : {}", LocalDateTime.now());
            Chapter chapter = chapterRepository.findById(topicRequestDto.getChapterId())
                            .orElseThrow(() -> new RuntimeException("Chapter Not Found !"));

            String topicImageUrl = uploadFile(topicRequestDto.getTopicImage(), IMAGE_UPLOAD_DIR);

            Topic topic = new Topic();
            topic.setTopicTitle(topicRequestDto.getTopicTitle())
                .setTopicImage(topicImageUrl)
                .setTopicVideoLink(topicRequestDto.getTopicVideoLink())
                .setTopicDescription(topicRequestDto.getTopicDescription())
                .setChapter(chapter);

            Topic saveTopic = topicRepository.save(topic);

            Set<TopicDocument> documents = new HashSet<>();
            if(topicRequestDto.getDocuments() != null){
                for(MultipartFile file : topicRequestDto.getDocuments()){
                    String uploadDir = isImage(file) ? IMAGE_UPLOAD_DIR : DOC_UPLOAD_DIR;
                    String documentUrl = uploadFile(file, uploadDir);
                    TopicDocument document = new TopicDocument();
                    document.setDocumentUrl(documentUrl);
                    document.setTopic(saveTopic);
                    document = topicDocumentRepository.save(document);
                    documents.add(document);
                }
            }

            return mapToDto(saveTopic, documents);

        } catch (Exception e) {
            logger.error("Cannot Save Topic: {}", LocalDateTime.now(), e.getMessage());
            throw new RuntimeException("Cannot Save Topic ! " + e.getMessage());
        }
    }

    public TopicResponseDto getTopicById(Integer id){
        try{
            Topic topic = topicRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Topic Not Found !"));
        
            List<TopicDocument> documentList = topicDocumentRepository.findByTopicId(id);

            Set<TopicDocument> documentSet = new HashSet<>(documentList);

            return mapToDto(topic, documentSet);
        } catch (Exception e) {
            logger.error("Cannot Get Topic By Id: {}", LocalDateTime.now(), e.getMessage());
            throw new RuntimeException("Cannot get Topic by Id ! " + e.getMessage());
        }
    }

    public List<TopicResponseDto> getAllTopic(){
        try{
            List<Topic> topicList = topicRepository.findAll();

            List<TopicResponseDto> topicResponseDtos = new ArrayList<TopicResponseDto>();

            for(Topic topic : topicList){
                List<TopicDocument> documentList = topicDocumentRepository.findByTopicId(topic.getId());

                Set<TopicDocument> documentSet = new HashSet<>(documentList);

                topicResponseDtos.add(mapToDto(topic, documentSet));
            }

            return topicResponseDtos;
        } catch (Exception e) {
            logger.error("Cannot Get Topic: {}", LocalDateTime.now(), e.getMessage());
            throw new RuntimeException("Cannot Get Topic ! " + e.getMessage());
        }
    }

    @Transactional
    public TopicResponseDto updateTopic(Integer id, TopicRequestDto topicRequestDto){
        try{
            Topic existingTopic = topicRepository.findById(id)
                                    .orElseThrow(() -> new RuntimeException("Topic Not Found In Update !"));
            existingTopic.setTopicTitle(topicRequestDto.getTopicTitle());
            existingTopic.setTopicDescription(topicRequestDto.getTopicDescription());
            existingTopic.setTopicVideoLink(topicRequestDto.getTopicVideoLink());

            if(topicRequestDto.getTopicImage() != null && !topicRequestDto.getTopicImage().isEmpty()){
                deleteFile(existingTopic.getTopicImage());

                String topicImageUrl = uploadFile(topicRequestDto.getTopicImage(), IMAGE_UPLOAD_DIR);
                existingTopic.setTopicImage(topicImageUrl);
            }

            Set<TopicDocument> documents = new HashSet<>();
            if(topicRequestDto.getDocuments() != null){
                for(MultipartFile file : topicRequestDto.getDocuments()){
                    String uploadDir = isImage(file) ? IMAGE_UPLOAD_DIR : DOC_UPLOAD_DIR;
                    String documentUrl = uploadFile(file, uploadDir);
                    TopicDocument document = new TopicDocument();
                    document.setDocumentUrl(documentUrl);
                    document.setTopic(existingTopic);
                    document = topicDocumentRepository.save(document);
                    documents.add(document);
                }
            }

            Topic updatedTopic = topicRepository.save(existingTopic);

            return mapToDto(updatedTopic, documents);

        } catch (Exception e) {
            logger.error("Cannot Update Topic: {}", LocalDateTime.now(), e.getMessage());
            throw new RuntimeException("Cannot Update Topic ! " + e.getMessage());
        }
    }

    @Transactional
    public void deleteTopic(Integer Id){
        try {
            Topic topic = topicRepository.findById(Id)
            .orElseThrow(() -> new RuntimeException("Topic Not Found in Deletion !"));
            List<TopicDocument> topicDocuments = topicDocumentRepository.findByTopicId(Id);
            for(TopicDocument topicDocument : topicDocuments){
                deleteFile(topicDocument.getDocumentUrl());
                topicDocumentRepository.delete(topicDocument);
            }

            deleteFile(topic.getTopicImage());

            topicRepository.delete(topic);   
        } catch (Exception e) {
            logger.error("Cannot Delete Topic: {}", LocalDateTime.now(), e.getMessage());
            throw new RuntimeException("Cannot Delete Topic ! " + e.getMessage());
        }
    }

    @Transactional
    public void deleteTopicDocument(Integer docId){
        try {
            TopicDocument topicDocument = topicDocumentRepository.findById(docId)
            .orElseThrow(() -> new RuntimeException("Topic Document Not Found in Topic Doucment Deletion !"));

            deleteFile(topicDocument.getDocumentUrl());

            topicDocumentRepository.delete(topicDocument);
        } catch (Exception e) {
            logger.error("Cannot Delete Topic Documents : {}", LocalDateTime.now(), e.getMessage());
            throw new RuntimeException("Cannot Delete Topic Documents ! " + e.getMessage());
        }
    }

    private String uploadFile(MultipartFile file, String uploadDir) throws IOException{
        if(file == null || file.isEmpty()){
            return null;
        }

        Path uplodPath = rootLocation.resolve(uploadDir);

        if(!Files.exists(uplodPath)){
            Files.createDirectories(uplodPath);
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uplodPath.resolve(fileName);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return uploadDir + fileName;
    }

    private boolean isImage(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    private void deleteFile(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            File file = new File("upload" + imageUrl);
            if (file.exists()) {
                file.delete();
            }
        }
    }


}
