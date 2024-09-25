package com.bussiness.advail_capitals.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bussiness.advail_capitals.entities.Inquiry;

public interface InquiryRepository extends JpaRepository<Inquiry, Integer>{
    
}
