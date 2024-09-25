package com.bussiness.advail_capitals.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bussiness.advail_capitals.dtos.InquiryRequestDto;
import com.bussiness.advail_capitals.dtos.InquiryResponseDto;
import com.bussiness.advail_capitals.entities.Inquiry;
import com.bussiness.advail_capitals.repositories.InquiryRepository;

import jakarta.transaction.Transactional;

@Service
public class InquiryService {

    private static final Logger logger = LoggerFactory.getLogger(InquiryService.class);

    @Autowired
    private InquiryRepository inquiryRepository;

    private InquiryResponseDto mapToDto(Inquiry inquiry){
        InquiryResponseDto dto = new InquiryResponseDto();
        dto.setId(inquiry.getId());
        dto.setEmail(inquiry.getEmail());
        dto.setUserName(inquiry.getUserName());
        dto.setBecamePartner(inquiry.isBecamePartner());
        dto.setWatch(inquiry.isWatch());
        dto.setMessage(inquiry.getMessage());
        dto.setPhoneNumber(inquiry.getPhoneNumber());
        return dto;
    }
    
    @Transactional
    public InquiryResponseDto createInquiry(InquiryRequestDto inquiryRequestDto){
        try {
            logger.info("Add Inquiry : {}", LocalDateTime.now());
            Inquiry inquiry = new Inquiry();
            inquiry.setUserName(inquiryRequestDto.getUserName())
                    .setEmail(inquiryRequestDto.getEmail())
                    .setBecamePartner(inquiryRequestDto.isBecamePartner())
                    .setPhoneNumber(inquiryRequestDto.getPhoneNumber())
                    .setMessage(inquiryRequestDto.getMessage())
                    .setWatch(false);
            Inquiry saveInquiry = inquiryRepository.save(inquiry);

            return mapToDto(saveInquiry);

        } catch (Exception e) {
            logger.error("Cannot add inquiry !", LocalDateTime.now(), e.getMessage());
            throw new RuntimeException("Cannot Add Inquiry !");
        }
    }

    @Transactional
    public void updateInquiry(Integer id){
        try {
            logger.info("Update Inquiry : {}", LocalDateTime.now());
            Inquiry updateInq = inquiryRepository.findById(id).orElseThrow(() -> new RuntimeException("Inquiry Not Found !")); ;
            updateInq.setWatch(true);
        } catch (Exception e) {
            logger.error("Cannot update inquiry !", LocalDateTime.now(), e.getMessage());
            throw new RuntimeException("Cannot update Inquiry !");
        }
    }

    public List<InquiryResponseDto> getAllInquiry(){
        try {
            logger.info("Get All Inquiry : {}", LocalDateTime.now());
            return inquiryRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Cannot get inquiries !", LocalDateTime.now(), e.getMessage());
            throw new RuntimeException("Cannot get Inquiries !");
        }
    }

}
