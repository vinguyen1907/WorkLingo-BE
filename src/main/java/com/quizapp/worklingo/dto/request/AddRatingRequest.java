package com.quizapp.worklingo.dto.request;

import com.quizapp.worklingo.enums.RatingType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddRatingRequest {
    private Integer userId;
    private Integer lessonId;
    private RatingType ratingType;
}
