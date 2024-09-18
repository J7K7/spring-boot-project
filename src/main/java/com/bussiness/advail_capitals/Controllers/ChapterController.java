package com.bussiness.advail_capitals.Controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bussiness.advail_capitals.dtos.ChapterRequestDto;
import com.bussiness.advail_capitals.dtos.ChapterResponseDto;
import com.bussiness.advail_capitals.services.ChapterService;

@RestController
@RequestMapping("/api/chapters")
public class ChapterController {

    @Autowired
    private ChapterService chapterService;

    @GetMapping
    public ResponseEntity<?> getAllChapters(){
        try{
            List<ChapterResponseDto> chapters = chapterService.getAllChapter();
                    return ResponseEntity.ok(chapters);
        } catch(Exception e){
            Map<String, Object> map = new HashMap<>();
            map.put("message", e.getMessage());
            map.put("status", false);
            return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getChapterById(@PathVariable Integer id){
        try{
            ChapterResponseDto chapter = chapterService.getChapterById(id);
            return ResponseEntity.ok(chapter);
        } catch(Exception e){
            Map<String, Object> map = new HashMap<>();
            map.put("message", e.getMessage());
            map.put("status", false);
            return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createChapter(@ModelAttribute ChapterRequestDto chapterRequestDto){
        try{
            ChapterResponseDto createdChapter = chapterService.createChapter(chapterRequestDto);
            return ResponseEntity.ok(createdChapter);
        } catch(IllegalArgumentException e){
            Map<String, Object> map = new HashMap<>();
            map.put("message", e.getMessage());
            map.put("status", false);
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        } catch(Exception e){
            Map<String, Object> map = new HashMap<>();
            map.put("message", e.getMessage());
            map.put("status", false);
            return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateChapter(@PathVariable Integer id, @ModelAttribute ChapterRequestDto chapterRequestDto){
        try{
            ChapterResponseDto updatedChapter = chapterService.updateChapter(id, chapterRequestDto);
            return ResponseEntity.ok(updatedChapter);
        } catch(IllegalArgumentException e){
            Map<String, Object> map = new HashMap<>();
            map.put("message", e.getMessage());
            map.put("status", false);
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        } catch(Exception e){
            Map<String, Object> map = new HashMap<>();
            map.put("message", e.getMessage());
            map.put("status", false);
            return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteChapter(@PathVariable Integer id){
        try{
            chapterService.deleteChapter(id);
            return ResponseEntity.noContent().build();
        } catch(IllegalArgumentException e){
            Map<String, Object> map = new HashMap<>();
            map.put("message", e.getMessage());
            map.put("status", false);
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        } catch(Exception e){
            Map<String, Object> map = new HashMap<>();
            map.put("message", e.getMessage());
            map.put("status", false);
            return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
