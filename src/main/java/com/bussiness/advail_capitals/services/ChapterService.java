package com.bussiness.advail_capitals.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bussiness.advail_capitals.dtos.ChapterRequestDto;
import com.bussiness.advail_capitals.dtos.ChapterResponseDto;
import com.bussiness.advail_capitals.entities.Chapter;
import com.bussiness.advail_capitals.repositories.ChapterRepository;

@Service
public class ChapterService {

    private static final Logger logger = LoggerFactory.getLogger(ChapterService.class);

    private final Path rootLocation = Paths.get("upload");
    
    @Autowired
    private ChapterRepository chapterRepository;

    private ChapterResponseDto mapToDto(Chapter chapter){
        ChapterResponseDto dto = new ChapterResponseDto();
        dto.setId(chapter.getId());
        dto.setTitle(chapter.getTitle());
        dto.setChapterImage(chapter.getChapterImage());
        dto.setTotalTime(chapter.getTotalTime());
        dto.setTotalVideo(chapter.getTotalVideo());
        return dto;
    }

    @Transactional
    public String storeImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Failed to store empty file");
        }
    
        // Create the 'upload/images' directory if it doesn't exist
        Path imageDirectory = rootLocation.resolve("images");
        if (!Files.exists(imageDirectory)) {
            Files.createDirectories(imageDirectory);
        }
    
        // Get the original file name and its extension
        String originalFileName = file.getOriginalFilename();
        @SuppressWarnings("null")
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        
        // Generate the current timestamp with seconds
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    
        // Create a new file name with the timestamp appended before the extension
        String newFileName = originalFileName.replace(extension, "") + "_" + timestamp + extension;
    
        // Save the file to 'upload/images' folder
        Path destinationFile = imageDirectory.resolve(newFileName).normalize().toAbsolutePath();
        Files.copy(file.getInputStream(), destinationFile);
    
        // Return the relative URL for accessing the image
        return "/images/" + newFileName;
    }
    


    public List<ChapterResponseDto> getAllChapter(){
        try{
            logger.info("Get All Chapters : {}", LocalDateTime.now());
            return chapterRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
        } catch(Exception  e){
            logger.error("Get All Chapter failed: {}", LocalDateTime.now(), e.getMessage());
            throw new RuntimeException("Cannot Get Chapters !");
        }
    } 

    public ChapterResponseDto getChapterById(Integer id){
        try {
            logger.info("Get Chapter By Id : {}", LocalDateTime.now());
            if(id == 0 || id == null){
                throw new IllegalArgumentException("Invalid Id !");
            }
            Chapter chapter = chapterRepository.findById(id).orElseThrow(() -> new RuntimeException("Chapter Not Found !"));   
            return mapToDto(chapter);
        } catch (IllegalArgumentException e) {
            logger.error("Get Chapter By Id failed for user Id: {}", LocalDateTime.now(), e.getMessage());
            throw new IllegalArgumentException("Invalid Id !");
        } catch (Exception e) {
            logger.error("Get Chapter By Id failed for user: {}", LocalDateTime.now(), e.getMessage());
            throw new RuntimeException("Chapter Not Found !");
        }
    }

    @Transactional
    public ChapterResponseDto createChapter(ChapterRequestDto chapterRequestDto){
        try {
            logger.info("Save Chapter : {}", LocalDateTime.now());
            String imageUrl = storeImage(chapterRequestDto.getChapterImage());
            Chapter chapter = new Chapter();
            chapter.setTitle(chapterRequestDto.getTitle())
                   .setChapterImage(imageUrl)
                   .setTotalTime(chapterRequestDto.getTotalTime())
                   .setTotalVideo(chapterRequestDto.getTotalVideo());
            Chapter savedChapter = chapterRepository.save(chapter);   
            return mapToDto(savedChapter);
        } catch (Exception e) {
            logger.error("Cannot Save Chapter: {}", LocalDateTime.now(), e.getMessage());
            throw new RuntimeException("Cannot Save Chapter !");
        }
    }

    @Transactional
    public ChapterResponseDto updateChapter(Integer id, ChapterRequestDto chapterRequestDto){
        try {
            logger.info("Update Chapter : {}", LocalDateTime.now());
            if(id == 0 || id == null){
                throw new IllegalArgumentException("Invalid Id !");
            }

            ChapterResponseDto updateChapter = getChapterById(id);
            
            Chapter exisitingChapter = new Chapter();
            exisitingChapter.setId(updateChapter.getId())
            .setChapterImage(updateChapter.getChapterImage())
            .setTitle(updateChapter.getTitle())
            .setTotalTime(updateChapter.getTotalTime())
            .setTotalVideo(updateChapter.getTotalVideo());

            if (chapterRequestDto.getChapterImage() != null && !chapterRequestDto.getChapterImage().isEmpty()) {
                deleteImage(exisitingChapter.getChapterImage());
                String imageUrl = storeImage(chapterRequestDto.getChapterImage());
                exisitingChapter.setChapterImage(imageUrl);
            }
            return mapToDto(chapterRepository.save(exisitingChapter));   
        } catch (IllegalArgumentException e) {
            logger.error("Cannot Update Chapter By Id: {}", LocalDateTime.now(), e.getMessage());
            throw new IllegalArgumentException("Invalid Id !");
        } catch (Exception e) {
            logger.error("Cannot Update Chapter: {}", LocalDateTime.now(), e.getMessage());
            throw new RuntimeException("Cannot Update Chapter !");
        }
    }

    @Transactional
    public void deleteChapter(Integer id){
        try {
            logger.info("Delete Chapter : {}", LocalDateTime.now());
            if(id == 0 || id == null){
                throw new IllegalArgumentException("Invalid Id !");
            }
            ChapterResponseDto chapterResponseDto = getChapterById(id);
            deleteImage(chapterResponseDto.getChapterImage());

            chapterRepository.deleteById(id);
        } catch (IllegalArgumentException e) {
            logger.error("Cannot Delete Chapter By Id: {}", LocalDateTime.now(), e.getMessage());
            throw new IllegalArgumentException("Invalid Id !");
        } catch (Exception e) {
            logger.error("Chapter Not Found : {}", LocalDateTime.now(), e.getMessage());
            throw new RuntimeException("Chapter Not Found !");
        }
    }

    public void deleteImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            File file = new File("upload" + imageUrl);
            if (file.exists()) {
                file.delete();
            }
        }
    }

}
