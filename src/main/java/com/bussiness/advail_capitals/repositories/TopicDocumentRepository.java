package com.bussiness.advail_capitals.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bussiness.advail_capitals.entities.TopicDocument;

public interface TopicDocumentRepository extends JpaRepository<TopicDocument, Integer>{
    List<TopicDocument> findByTopicId(Integer TopicId); 
}
