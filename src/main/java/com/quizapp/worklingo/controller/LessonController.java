package com.quizapp.worklingo.controller;

import com.quizapp.worklingo.model.Flashcard;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/api/v1/lessons")
public class LessonController {
    @GetMapping("/{lessonId}/flashcards")
    public ResponseEntity<List<Flashcard>> getFlashcardInLesson(
            @PathVariable Integer lessonId
    ) {
        return ResponseEntity.ok(lessonService.getFlashcardsInLesson(lessonId));
    }

}
