package com.quizapp.worklingo.controller;

import com.quizapp.worklingo.dto.PageDTO;
import com.quizapp.worklingo.dto.RecentLessonDTO;
import com.quizapp.worklingo.dto.request.RecentLessonRequestDTO;
import com.quizapp.worklingo.service.interfaces.IRecentLessonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/lessons/recent")
@RequiredArgsConstructor
@Tag(name = "Recent lessons")
public class RecentLessonController {
    private final IRecentLessonService recentLessonService;

    @GetMapping()
    @Operation(summary = "This method is used to get recent lessons of an user.")
    public ResponseEntity<PageDTO<RecentLessonDTO>> getRecentLessons(
            @RequestParam Integer userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(recentLessonService.getRecentLessons(userId, page, size));
    }

    @PostMapping()
    @Operation(summary = "This method is used to create a new recent lessons to an user.")
    public ResponseEntity<RecentLessonDTO> addRecentLesson(
            @RequestBody RecentLessonRequestDTO request
    ) {
        return ResponseEntity.ok(recentLessonService.addRecentLesson(request));
    }
}
