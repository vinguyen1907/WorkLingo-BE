package com.quizapp.worklingo.service;

import com.quizapp.worklingo.dto.RatingDTO;
import com.quizapp.worklingo.enums.RatingType;
import com.quizapp.worklingo.model.Lesson;
import com.quizapp.worklingo.model.Rating;
import com.quizapp.worklingo.model.RatingId;
import com.quizapp.worklingo.model.user.User;
import com.quizapp.worklingo.repository.LessonRepository;
import com.quizapp.worklingo.repository.RatingRepository;
import com.quizapp.worklingo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LessonRepository lessonRepository;

    @InjectMocks
    private RatingService ratingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddRating_NewRating() {
        Integer userId = 1;
        Integer lessonId = 1;
        RatingType ratingType = RatingType.UPVOTE;

        when(ratingRepository.findAllByUserIdAndLessonId(userId, lessonId)).thenReturn(Optional.empty());
        User user = new User();
        Lesson lesson = new Lesson();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));
        Rating rating = new Rating(new RatingId(userId, lessonId), ratingType, user, lesson, LocalDateTime.now());
        when(ratingRepository.save(any(Rating.class))).thenReturn(rating);

        RatingDTO result = ratingService.addRating(userId, lessonId, ratingType);

        assertNotNull(result);
        verify(ratingRepository, times(1)).findAllByUserIdAndLessonId(userId, lessonId);
        verify(ratingRepository, times(1)).save(any(Rating.class));
    }

    @Test
    void testAddRating_ExistingSameRating() {
        Integer userId = 1;
        Integer lessonId = 1;
        RatingType ratingType = RatingType.UPVOTE;

        Rating existingRating = new Rating(new RatingId(userId, lessonId), ratingType, new User(), new Lesson(), LocalDateTime.now());
        when(ratingRepository.findAllByUserIdAndLessonId(userId, lessonId)).thenReturn(Optional.of(existingRating));

        assertThrows(IllegalArgumentException.class, () -> {
            ratingService.addRating(userId, lessonId, ratingType);
        });

        verify(ratingRepository, times(1)).findAllByUserIdAndLessonId(userId, lessonId);
    }

    @Test
    void testAddRating_ExistingDifferentRating() {
        Integer userId = 1;
        Integer lessonId = 1;
        RatingType oldRatingType = RatingType.UPVOTE;
        RatingType newRatingType = RatingType.DOWNVOTE;

        User user = new User();
        Lesson lesson = new Lesson();
        lesson.setNumberOfUpVotes(1);
        lesson.setNumberOfDownVotes(0);

        Rating existingRating = new Rating(new RatingId(userId, lessonId), oldRatingType, user, lesson, LocalDateTime.now());
        when(ratingRepository.findAllByUserIdAndLessonId(userId, lessonId)).thenReturn(Optional.of(existingRating));
        when(ratingRepository.save(any(Rating.class))).thenReturn(existingRating);
        when(lessonRepository.save(any(Lesson.class))).thenReturn(lesson);

        RatingDTO result = ratingService.addRating(userId, lessonId, newRatingType);

        assertNotNull(result);
        assertEquals(newRatingType, result.getRatingType());
        verify(ratingRepository, times(1)).findAllByUserIdAndLessonId(userId, lessonId);
        verify(ratingRepository, times(1)).save(any(Rating.class));
        verify(lessonRepository, times(1)).save(any(Lesson.class));
    }

    @Test
    void testRemoveRating() {
        Integer userId = 1;
        Integer lessonId = 1;

        Rating rating = new Rating(new RatingId(userId, lessonId), RatingType.UPVOTE, new User(), new Lesson(), LocalDateTime.now());
        when(ratingRepository.findAllByUserIdAndLessonId(userId, lessonId)).thenReturn(Optional.of(rating));

        ratingService.removeRating(userId, lessonId);

        verify(ratingRepository, times(1)).findAllByUserIdAndLessonId(userId, lessonId);
        verify(ratingRepository, times(1)).delete(rating);
    }

    @Test
    void testRemoveRating_NotFound() {
        Integer userId = 1;
        Integer lessonId = 1;

        when(ratingRepository.findAllByUserIdAndLessonId(userId, lessonId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            ratingService.removeRating(userId, lessonId);
        });

        verify(ratingRepository, times(1)).findAllByUserIdAndLessonId(userId, lessonId);
    }

    @Test
    void testCheckUserRating_Found() {
        Integer userId = 1;
        Integer lessonId = 1;

        Rating rating = new Rating(new RatingId(userId, lessonId), RatingType.UPVOTE, new User(), new Lesson(), LocalDateTime.now());
        when(ratingRepository.findById(any(RatingId.class))).thenReturn(Optional.of(rating));
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(new Lesson()));

        RatingDTO result = ratingService.checkUserRating(userId, lessonId);

        assertEquals(rating.getRatingType(), result.getRatingType());
        verify(ratingRepository, times(1)).findById(any(RatingId.class));
    }

    @Test
    void testCheckUserRating_NotFound() {
        Integer userId = 1;
        Integer lessonId = 1;

        when(ratingRepository.findById(new RatingId(userId, lessonId))).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(new Lesson()));

        RatingDTO result = ratingService.checkUserRating(userId, lessonId);

        assertNull(result);
        verify(ratingRepository, times(1)).findById(any(RatingId.class));
    }

    @Test
    void testCheckUserRating_UserNotFound() {
        Integer userId = 1;
        Integer lessonId = 1;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            ratingService.checkUserRating(userId, lessonId);
        });

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testCheckUserRating_LessonNotFound() {
        Integer userId = 1;
        Integer lessonId = 1;

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            ratingService.checkUserRating(userId, lessonId);
        });

        verify(userRepository, times(1)).findById(userId);
        verify(lessonRepository, times(1)).findById(lessonId);
    }
}
