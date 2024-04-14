package com.quizapp.worklingo.repository;

import com.quizapp.worklingo.model.Flashcard;
import com.quizapp.worklingo.model.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard, Integer> {
    Page<Flashcard> findAllByLessonId(Integer lessonId, Pageable pageable);
}
