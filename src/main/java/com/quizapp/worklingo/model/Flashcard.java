package com.quizapp.worklingo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Flashcard {
    @Id
    private Integer id;
    private String question;
    private String answer;

    private Lesson flashcardList;
}
