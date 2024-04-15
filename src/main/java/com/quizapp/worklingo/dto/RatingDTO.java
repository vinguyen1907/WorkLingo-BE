package com.quizapp.worklingo.dto;

import com.quizapp.worklingo.enums.RatingType;
import com.quizapp.worklingo.model.Rating;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RatingDTO {
    private Integer userId;
    private Integer lessonId;
    private RatingType ratingType;
    private LocalDateTime createdAt;

    public RatingDTO(Rating rating) {
        this.userId = rating.getUser().getId();
        this.lessonId = rating.getLesson().getId();
        this.ratingType = rating.getRatingType();
        this.createdAt = rating.getUpdatedTime();
    }
}
