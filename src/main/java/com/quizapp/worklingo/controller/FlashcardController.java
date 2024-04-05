package com.quizapp.worklingo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1/flashcards")
public class FlashcardController {
    @GetMapping()
    public ResponseEntity getFlashcardsInList(

    ) {
        return ResponseEntity.ok().build();
    }
}
