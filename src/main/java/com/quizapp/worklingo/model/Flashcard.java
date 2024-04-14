package com.quizapp.worklingo.model;

import com.quizapp.worklingo.dto.FlashcardDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Flashcard {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String question;

    private String answer;

    @ManyToOne
    private Lesson lesson;

    public FlashcardDTO toDTO() {
        return new FlashcardDTO(this);
    }
}
