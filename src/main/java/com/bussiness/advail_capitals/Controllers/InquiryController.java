package com.bussiness.advail_capitals.Controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bussiness.advail_capitals.dtos.InquiryRequestDto;
import com.bussiness.advail_capitals.dtos.InquiryResponseDto;
import com.bussiness.advail_capitals.services.InquiryService;

@RestController
@RequestMapping("/api/inquiry")
public class InquiryController {
    @Autowired
    InquiryService inquiryService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllInquiry(){
        try {
            List<InquiryResponseDto> inquireResponseList = inquiryService.getAllInquiry();
            return ResponseEntity.ok(inquireResponseList);
        } catch (Exception e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", e.getMessage());
            map.put("status", false);
            return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addInquiry")
    public ResponseEntity<?> createInquiry(@ModelAttribute InquiryRequestDto inquiryRequestDto){
        try {
            InquiryResponseDto inquiryResponseDto = inquiryService.createInquiry(inquiryRequestDto);
            return ResponseEntity.ok(inquiryResponseDto);
        } catch (Exception e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", e.getMessage());
            map.put("status", false);
            return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateInquiry(@PathVariable Integer id){
        try {
            inquiryService.updateInquiry(id);
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Updated !");
            return ResponseEntity.ok(map);
        } catch (Exception e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", e.getMessage());
            map.put("status", false);
            return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
