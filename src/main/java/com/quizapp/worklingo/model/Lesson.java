package com.quizapp.worklingo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Lesson {
    @Id
    private Integer id;
    @OneToOne
    private Topic topic;
    private Integer authorId;
    private Double averageRating;
    private Integer numberOfRatings;
    private Integer numberOfFlashcards;
    @OneToMany
    private List<Flashcard> flashcards;
}
