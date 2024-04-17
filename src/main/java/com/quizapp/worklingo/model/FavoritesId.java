package com.quizapp.worklingo.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class FavoritesId implements Serializable {
    private Integer userId;
    private Integer lessonId;
}
