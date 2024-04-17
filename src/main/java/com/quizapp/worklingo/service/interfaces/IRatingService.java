package com.quizapp.worklingo.service.interfaces;

import com.quizapp.worklingo.dto.RatingDTO;
import com.quizapp.worklingo.enums.RatingType;

public interface IRatingService {
    RatingDTO addRating(Integer userId, Integer lessonId, RatingType ratingType);
    void removeRating(Integer userId, Integer lessonId);
//    void updateRating(Integer userId, Integer lessonId, Integer rating);
//    void deleteRating(Integer userId, Integer lessonId);
//    Integer getRating(Integer userId, Integer lessonId);
//    Double getAverageRating(Integer lessonId);
//    Integer getRatingCount(Integer lessonId);
//    Integer getRatingSum(Integer lessonId);
}
