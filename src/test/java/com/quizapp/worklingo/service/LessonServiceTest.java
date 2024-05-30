package com.quizapp.worklingo.service;

import com.quizapp.worklingo.dto.FlashcardDTO;
import com.quizapp.worklingo.dto.LessonDTO;
import com.quizapp.worklingo.dto.PageDTO;
import com.quizapp.worklingo.dto.RecentLessonDTO;
import com.quizapp.worklingo.dto.request.CreateLessonRequest;
import com.quizapp.worklingo.dto.request.UpdateLessonRequest;
import com.quizapp.worklingo.enums.LessonVisibility;
import com.quizapp.worklingo.model.Flashcard;
import com.quizapp.worklingo.model.Lesson;
import com.quizapp.worklingo.model.RecentLesson;
import com.quizapp.worklingo.model.Topic;
import com.quizapp.worklingo.model.user.User;
import com.quizapp.worklingo.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LessonServiceTest {

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private FlashcardRepository flashcardRepository;

    @Mock
    private RecentLessonRepository recentLessonRepository;

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LessonService lessonService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetTopRatingLessons() {
        Pageable pageable = PageRequest.of(0, 10);
        User user = new User();
        user.setId(1);
        Lesson lesson = new Lesson();
        lesson.setId(1);
        lesson.setAuthor(user);
        Page<Lesson> lessonPage = new PageImpl<>(List.of(lesson));
        when(lessonRepository.findAllByVisibilityOrderByNumberOfUpVotesDesc(LessonVisibility.PUBLIC, pageable)).thenReturn(lessonPage);

        PageDTO<LessonDTO> result = lessonService.getTopRatingLessons(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getResults().size());
        verify(lessonRepository, times(1)).findAllByVisibilityOrderByNumberOfUpVotesDesc(LessonVisibility.PUBLIC, pageable);
    }

    @Test
    public void testGetLessonById_Found() {
        User user = new User();
        user.setId(1);
        int lessonId = 1;
        Lesson lesson = new Lesson();
        lesson.setId(lessonId);
        lesson.setAuthor(user);
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));

        LessonDTO result = lessonService.getLessonById(lessonId);

        assertNotNull(result);
        assertEquals(lessonId, result.getId());
        verify(lessonRepository, times(1)).findById(lessonId);
    }

    @Test
    public void testGetLessonById_NotFound() {
        int lessonId = 1;
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            lessonService.getLessonById(lessonId);
        });

        verify(lessonRepository, times(1)).findById(lessonId);
    }

    @Test
    public void testGetFlashcardsInLesson() {
        int lessonId = 1;
        Flashcard flashcard = new Flashcard();
        flashcard.setId(1);
        Lesson lesson = new Lesson();
        lesson.setId(1);
        flashcard.setLesson(lesson);
        when(flashcardRepository.findAllByLessonId(lessonId)).thenReturn(List.of(flashcard));

        List<FlashcardDTO> result = lessonService.getFlashcardsInLesson(lessonId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(flashcardRepository, times(1)).findAllByLessonId(lessonId);
    }

    @Test
    public void testGetRecentLessons() {
        int userId = 1;
        Pageable pageable = PageRequest.of(0, 10);
        RecentLesson recentLesson = new RecentLesson();
        recentLesson.setId(1);
        User user = new User();
        user.setId(1);
        Lesson lesson = new Lesson();
        lesson.setId(1);
        lesson.setAuthor(user);
        recentLesson.setUser(user);
        recentLesson.setLesson(lesson);
        Page<RecentLesson> recentLessonPage = new PageImpl<>(List.of(recentLesson));
        when(recentLessonRepository.findAllByUserIdOrderByLastAccessedDesc(userId, pageable)).thenReturn(recentLessonPage);

        PageDTO<RecentLessonDTO> result = lessonService.getRecentLessons(userId, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getResults().size());
        verify(recentLessonRepository, times(1)).findAllByUserIdOrderByLastAccessedDesc(userId, pageable);
    }

    @Test
    public void testCreateLesson() {
        CreateLessonRequest request = new CreateLessonRequest();
        request.setTitle("Test Lesson");
        request.setTopicId(1);
        request.setAuthorId(1);
        request.setFlashcards(Collections.emptyList());

        Topic topic = new Topic();
        topic.setId(1);
        topic.setNumberOfLessons(5);
        when(topicRepository.findById(1)).thenReturn(Optional.of(topic));

        User user = new User();
        user.setId(1);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        Lesson lesson = new Lesson();
        lesson.setId(1);
        lesson.setAuthor(user);
        when(lessonRepository.save(any(Lesson.class))).thenReturn(lesson);

        LessonDTO result = lessonService.createLesson(request);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(topicRepository, times(1)).findById(1);
        verify(userRepository, times(1)).findById(1);
        verify(lessonRepository, times(1)).save(any(Lesson.class));
    }

    @Test
    public void testUpdateLesson() {
        int lessonId = 1;
        UpdateLessonRequest request = new UpdateLessonRequest(
                "Updated Lesson",
                LessonVisibility.PUBLIC
        );
        User user = new User();
        user.setId(1);
        Lesson lesson = new Lesson();
        lesson.setId(lessonId);
        lesson.setAuthor(user);
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));
        when(lessonRepository.save(any(Lesson.class))).thenReturn(lesson);

        LessonDTO result = lessonService.updateLesson(lessonId, request);

        assertNotNull(result);
        assertEquals(lessonId, result.getId());
        verify(lessonRepository, times(1)).findById(lessonId);
        verify(lessonRepository, times(1)).save(any(Lesson.class));
    }

    @Test
    public void testDeleteLesson() {
        int lessonId = 1;
        Lesson lesson = new Lesson();
        lesson.setId(lessonId);
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));

        lessonService.deleteLesson(lessonId);

        verify(lessonRepository, times(1)).findById(lessonId);
        verify(lessonRepository, times(1)).delete(lesson);
    }

    @Test
    public void testDeleteFlashcardInLesson() {
        int lessonId = 1;
        int flashcardId = 1;
        Flashcard flashcard = new Flashcard();
        flashcard.setId(flashcardId);
        Lesson lesson = new Lesson();
        lesson.setId(lessonId);
        lesson.setNumberOfFlashcards(5);
        flashcard.setLesson(lesson);

        when(flashcardRepository.findById(flashcardId)).thenReturn(Optional.of(flashcard));
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));

        lessonService.deleteFlashcardInLesson(lessonId, flashcardId);

        verify(flashcardRepository, times(1)).findById(flashcardId);
        verify(flashcardRepository, times(1)).delete(flashcard);
        verify(lessonRepository, times(1)).findById(lessonId);
        verify(lessonRepository, times(1)).save(lesson);
    }

    @Test
    public void testGetOwnLessons() {
        int userId = 1;
        Pageable pageable = PageRequest.of(0, 10);
        User user = new User();
        user.setId(1);
        Lesson lesson = new Lesson();
        lesson.setId(1);
        lesson.setAuthor(user);
        Page<Lesson> lessonPage = new PageImpl<>(List.of(lesson));
        when(lessonRepository.findAllByAuthorIdOrderByUpdatedTimeDesc(userId, pageable)).thenReturn(lessonPage);

        PageDTO<LessonDTO> result = lessonService.getOwnLessons(userId, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getResults().size());
        verify(lessonRepository, times(1)).findAllByAuthorIdOrderByUpdatedTimeDesc(userId, pageable);
    }
}
