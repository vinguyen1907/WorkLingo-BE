package com.quizapp.worklingo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Topic {
    @Id
    private Integer id;
    private String name;
    private String description;
    private String imageUrl;
    private Integer numberOfLessons; // 1 lesson == 1 flashcard list
}
