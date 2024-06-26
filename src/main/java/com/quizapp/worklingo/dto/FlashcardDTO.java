package com.quizapp.worklingo.dto;

import com.quizapp.worklingo.model.Flashcard;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlashcardDTO {
    private Integer id;

    private String question;

    private String answer;

    private Integer lessonId;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    public FlashcardDTO(Flashcard flashcard) {
        this.id = flashcard.getId();
        this.question = flashcard.getQuestion();
        this.answer = flashcard.getAnswer();
        this.lessonId = flashcard.getLesson().getId();
        this.createdTime = flashcard.getCreatedTime();
        this.updatedTime = flashcard.getUpdatedTime();
    }
}
