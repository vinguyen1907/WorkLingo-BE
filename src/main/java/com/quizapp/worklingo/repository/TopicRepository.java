package com.quizapp.worklingo.repository;

import com.quizapp.worklingo.model.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer> {
    Page<Topic> findAllByNameContainingIgnoreCase(String name, Pageable pageable);
}
