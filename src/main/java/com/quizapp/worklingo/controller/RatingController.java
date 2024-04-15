package com.quizapp.worklingo.controller;

import com.quizapp.worklingo.dto.RatingDTO;
import com.quizapp.worklingo.dto.request.AddRatingRequest;
import com.quizapp.worklingo.service.interfaces.IRatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ratings")
@RequiredArgsConstructor
@Tag(name = "Rating")
public class RatingController {
    private final IRatingService ratingService;

    @PostMapping()
    @Operation(summary = "Create a new rating.")
    public ResponseEntity<RatingDTO> addRating(
            @RequestBody AddRatingRequest request
    ) {
        return ResponseEntity.ok(ratingService.addRating(
                request.getUserId(),
                request.getLessonId(),
                request.getRatingType()
        ));
    }

    @DeleteMapping()
    @Operation(summary = "Delete a rating.")
    public ResponseEntity<?> deleteRating(
            @RequestParam Integer userId,
            @RequestParam Integer lessonId
    ) {
        ratingService.removeRating(userId, lessonId);
        return ResponseEntity.noContent().build();
    }
}
