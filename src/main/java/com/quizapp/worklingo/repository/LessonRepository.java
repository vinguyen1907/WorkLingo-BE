package com.quizapp.worklingo.repository;

import com.quizapp.worklingo.model.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    Page<Lesson> findAllByTopicId(Integer topicId, Pageable pageable);

    Page<Lesson> findAllByOrderByNumberOfUpVotesDesc(Pageable pageable);
}
