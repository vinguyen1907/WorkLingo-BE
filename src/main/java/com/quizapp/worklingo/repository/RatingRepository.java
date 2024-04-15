package com.quizapp.worklingo.repository;

import com.quizapp.worklingo.model.Rating;
import com.quizapp.worklingo.model.RatingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, RatingId> {
    Optional<Rating> findAllByUserIdAndLessonId(Integer userId, Integer lessonId);
}
