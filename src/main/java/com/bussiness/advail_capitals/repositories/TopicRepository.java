package com.bussiness.advail_capitals.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bussiness.advail_capitals.entities.Topic;

public interface TopicRepository extends JpaRepository<Topic, Integer>{
    List<Topic> findByChapterId(Integer chapterId);
}
