package com.quizapp.worklingo.controller;

import com.quizapp.worklingo.dto.FlashcardDTO;
import com.quizapp.worklingo.service.interfaces.IFlashcardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/flashcards")
@RequiredArgsConstructor
@Tag(name = "Flashcards")
public class FlashcardController {
    private final IFlashcardService flashcardService;

    @GetMapping("/{id}")
    @Operation(summary = "This method is used to get a flashcard by id.")
    public ResponseEntity<FlashcardDTO> getFlashcardById(
        @PathVariable int id
    ) {
        return ResponseEntity.ok(flashcardService.getFlashcardById(id));
    }
}
