package com.quizapp.worklingo.controller;

import com.quizapp.worklingo.dto.FlashcardDTO;
import com.quizapp.worklingo.model.Flashcard;
import com.quizapp.worklingo.service.interfaces.IFlashcardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    @Operation(summary = "This method is used to get all flashcards.")
    public ResponseEntity<List<FlashcardDTO>> getAllFlashcards() {
        return ResponseEntity.ok(flashcardService.getAllFlashcards());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "This method is used to delete a flashcard by id.")
    public ResponseEntity<Void> deleteFlashcardById(
        @PathVariable int id
    ) {
        flashcardService.deleteFlashcardById(id);
        return ResponseEntity.ok().build();
    }
}
