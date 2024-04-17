package com.quizapp.worklingo.repository;

import com.quizapp.worklingo.model.Flashcard;
import com.quizapp.worklingo.model.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard, Integer> {
    List<Flashcard> findAllByLessonId(Integer lessonId);
}
