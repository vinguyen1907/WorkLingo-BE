package com.quizapp.worklingo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<com.quizapp.worklingo.model.Lesson, Integer> {
    Page<com.quizapp.worklingo.model.Lesson> findAllByTopicId(Integer topicId, Pageable pageable);
}
