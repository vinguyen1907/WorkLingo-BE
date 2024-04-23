package com.quizapp.worklingo.service;

import com.quizapp.worklingo.dto.RatingDTO;
import com.quizapp.worklingo.enums.RatingType;
import com.quizapp.worklingo.model.Lesson;
import com.quizapp.worklingo.model.Rating;
import com.quizapp.worklingo.model.RatingId;
import com.quizapp.worklingo.repository.LessonRepository;
import com.quizapp.worklingo.repository.RatingRepository;
import com.quizapp.worklingo.repository.UserRepository;
import com.quizapp.worklingo.service.interfaces.IRatingService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RatingService implements IRatingService {
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;

    @Override
    @Transactional
    public RatingDTO addRating(Integer userId, Integer lessonId, RatingType ratingType) {
        // Case 1: This user has already rated this lesson
        // 1.1. Rating already exists, type = ratingType => error
        // 1.2. Rating already exists, type != ratingType => update rating
        // Case 2: Rating does not exist => create rating
        var rating = ratingRepository.findAllByUserIdAndLessonId(userId, lessonId);
        boolean hadRating = rating.isPresent();
        if (hadRating) {
            if (rating.get().getRatingType() == ratingType) {
                throw new IllegalArgumentException("Rating already exists");
            } else {
                return updateRating(
                        rating.get(),
                        rating.get().getLesson(),
                        rating.get().getRatingType(),
                        ratingType
                ).toDTO();
            }
        }

        return createNewRating(userId, lessonId, ratingType);
    }

    @Override
    public void removeRating(Integer userId, Integer lessonId) {
        var rating = ratingRepository.findAllByUserIdAndLessonId(userId, lessonId).orElseThrow(() -> new EntityNotFoundException("Rating not found"));
        ratingRepository.delete(rating);
    }

    @Override
    public RatingDTO checkUserRating(Integer userId, Integer lessonId) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        lessonRepository.findById(lessonId).orElseThrow(() -> new EntityNotFoundException("Lesson not found"));
        Optional<Rating> rating = ratingRepository.findById(new RatingId(userId, lessonId));
        return rating.map(Rating::toDTO).orElse(null);
    }

    private RatingDTO createNewRating(Integer userId, Integer lessonId, RatingType ratingType) {
        var user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        var lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new EntityNotFoundException("Lesson not found"));

        return ratingRepository.save(
                new Rating(
                        new RatingId(userId, lessonId),
                        ratingType,
                        user,
                        lesson,
                        LocalDateTime.now())
        ).toDTO();
    }

    private Rating updateRating(Rating rating, Lesson lesson, RatingType oldRatingType, RatingType newRatingType) {
        int currentUpvotes = lesson.getNumberOfUpVotes();
        int currentDownvotes = lesson.getNumberOfDownVotes();

        if (oldRatingType == RatingType.UPVOTE) {
            lesson.setNumberOfUpVotes(currentUpvotes - 1);
            lesson.setNumberOfDownVotes(currentDownvotes + 1);
        } else {
            lesson.setNumberOfDownVotes(currentDownvotes - 1);
            lesson.setNumberOfUpVotes(currentUpvotes + 1);
        }
        lessonRepository.save(lesson);

        rating.setRatingType(newRatingType);
        return ratingRepository.save(rating);
    }
}
