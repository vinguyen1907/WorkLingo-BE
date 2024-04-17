package com.quizapp.worklingo.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class RatingId implements Serializable {
    private Integer userId;
    private Integer lessonId;
}
