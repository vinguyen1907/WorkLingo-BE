package com.quizapp.worklingo.controller;

import com.quizapp.worklingo.dto.FlashcardDTO;
import com.quizapp.worklingo.dto.LessonDTO;
import com.quizapp.worklingo.dto.PageDTO;
import com.quizapp.worklingo.dto.RecentLessonDTO;
import com.quizapp.worklingo.model.Lesson;
import com.quizapp.worklingo.service.interfaces.ILessonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lessons")
@RequiredArgsConstructor
@Tag(name = "Lessons")
public class LessonController {
    private final ILessonService lessonService;

    @GetMapping("/top-rating")
    @Operation(summary = "This method is used to get top rating lessons.")
    public ResponseEntity<PageDTO<LessonDTO>> getTopRatingLessons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(lessonService.getTopRatingLessons(page, size));
    }

    @GetMapping("/{lessonId}")
    @Operation(summary = "This method is used to get a lesson by id.")
    public ResponseEntity<LessonDTO> getLessonById(
            @PathVariable Integer lessonId
    ) {
        return ResponseEntity.ok(lessonService.getLessonById(lessonId));
    }

    @GetMapping("/{lessonId}/flashcards")
    @Operation(summary = "This method is used to get flashcards in a lesson.")
    public ResponseEntity<List<FlashcardDTO>> getFlashcardsInLesson(
            @PathVariable Integer lessonId
    ) {
        return ResponseEntity.ok(lessonService.getFlashcardsInLesson(lessonId));
    }
}
