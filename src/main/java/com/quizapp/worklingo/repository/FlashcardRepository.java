package com.quizapp.worklingo.repository;

import com.quizapp.worklingo.model.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard, Integer> {

}
