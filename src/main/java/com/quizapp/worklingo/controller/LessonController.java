package com.quizapp.worklingo.controller;

import com.quizapp.worklingo.dto.FlashcardDTO;
import com.quizapp.worklingo.dto.LessonDTO;
import com.quizapp.worklingo.dto.PageDTO;
import com.quizapp.worklingo.dto.request.CreateLessonRequest;
import com.quizapp.worklingo.dto.request.UpdateLessonRequest;
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

    @GetMapping
    @Operation(summary = "This method is used to get all lessons.")
    public ResponseEntity<PageDTO<LessonDTO>> getAllLessons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(lessonService.getAllLessons(page, size));
    }


    @PostMapping()
    @Operation(summary = "Create a new lesson.")
    public ResponseEntity<LessonDTO> createLesson(
            @RequestBody CreateLessonRequest request
    ) {
        return ResponseEntity.ok(lessonService.createLesson(request));
    }

    @PutMapping("/{lessonId}")
    @Operation(summary = "Update a lesson.")
    public ResponseEntity<LessonDTO> updateLesson(
            @PathVariable Integer lessonId,
            @RequestBody UpdateLessonRequest request
    ) {
        return ResponseEntity.ok(lessonService.updateLesson(lessonId, request));
    }

    @DeleteMapping("/{lessonId}")
    @Operation(summary = "Delete a lesson.")
    public ResponseEntity<Void> deleteLesson(
            @PathVariable Integer lessonId
    ) {
        lessonService.deleteLesson(lessonId);
        return ResponseEntity.noContent().build();
    }
}
