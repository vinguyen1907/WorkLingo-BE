package com.quizapp.worklingo.controller;

import com.quizapp.worklingo.dto.FlashcardDTO;
import com.quizapp.worklingo.service.interfaces.IFlashcardService;
import com.quizapp.worklingo.service.interfaces.ILessonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lessons/{lessonId}/flashcards")
@RequiredArgsConstructor
@Tag(name = "Flashcards in lesson")
public class FlashcardsInLessonController {
    private final IFlashcardService flashcardService;
    private final ILessonService lessonService;

    @GetMapping()
    @Operation(summary = "Get flashcards in a lesson.")
    public ResponseEntity<List<FlashcardDTO>> getFlashcardsInLesson(
            @PathVariable Integer lessonId
    ) {
        return ResponseEntity.ok(lessonService.getFlashcardsInLesson(lessonId));
    }

    @DeleteMapping("/{flashcardId}")
    @Operation(summary = "Delete a flashcard in a lesson.")
    public ResponseEntity<Void> deleteFlashcardInLesson(
            @PathVariable Integer lessonId,
            @PathVariable Integer flashcardId
    ) {
        lessonService.deleteFlashcardInLesson(lessonId, flashcardId);
        return ResponseEntity.ok().build();
    }
}
