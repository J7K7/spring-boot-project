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

import com.bussiness.advail_capitals.dtos.TopicRequestDto;
import com.bussiness.advail_capitals.dtos.TopicResponseDto;
import com.bussiness.advail_capitals.services.TopicService;

@RestController
@RequestMapping("/api/topics")
public class TopicController {
    
    @Autowired
    TopicService topicService;

    @GetMapping
    public ResponseEntity<?> getAllTopic(){
        try {
            List<TopicResponseDto> topicResponseDtos = topicService.getAllTopic();
            return ResponseEntity.ok(topicResponseDtos);
        } catch (Exception e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", e.getMessage());
            map.put("status", false);
            return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getTopicById(@PathVariable Integer id){
        try {
            TopicResponseDto topicResponseDto = topicService.getTopicById(id);
            return ResponseEntity.ok(topicResponseDto);
        } catch (Exception e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", e.getMessage());
            map.put("status", false);
            return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createTopic(@ModelAttribute TopicRequestDto topicRequestDto){
        try {
            TopicResponseDto topicResponseDto = topicService.CreateTopic(topicRequestDto);
            return ResponseEntity.ok(topicResponseDto);
        } catch (Exception e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", e.getMessage());
            map.put("status", false);
            return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("{/id}")
    public ResponseEntity<?> updateTopic(@PathVariable Integer id, @ModelAttribute TopicRequestDto topicRequestDto){
        try {
            TopicResponseDto topicResponseDto = topicService.updateTopic(id, topicRequestDto);
            return ResponseEntity.ok(topicResponseDto);
        } catch (Exception e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", e.getMessage());
            map.put("status", false);
            return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("{/id}")
    public ResponseEntity<?> deleteTopic(@PathVariable Integer id){
        try {
            topicService.deleteTopic(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", e.getMessage());
            map.put("status", false);
            return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(("/doc{/id}"))
    public ResponseEntity<?> deleteTopicDocument(@PathVariable Integer id){
        try {
            topicService.deleteTopicDocument(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", e.getMessage());
            map.put("status", false);
            return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
