package com.quizapp.worklingo.service;

import com.quizapp.worklingo.dto.PageDTO;
import com.quizapp.worklingo.dto.RecentLessonDTO;
import com.quizapp.worklingo.dto.request.RecentLessonRequestDTO;
import com.quizapp.worklingo.model.Lesson;
import com.quizapp.worklingo.model.RecentLesson;
import com.quizapp.worklingo.model.user.User;
import com.quizapp.worklingo.repository.LessonRepository;
import com.quizapp.worklingo.repository.RecentLessonRepository;
import com.quizapp.worklingo.repository.UserRepository;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecentLessonServiceTest {

    @Mock
    private RecentLessonRepository recentLessonRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LessonRepository lessonRepository;

    @InjectMocks
    private RecentLessonService recentLessonService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRecentLessons() {
        Integer userId = 1;
        int page = 0;
        int size = 10;
        User user = new User();
        user.setId(1);
        Lesson lesson = new Lesson();
        lesson.setId(1);
        lesson.setAuthor(user);
        Pageable pageable = PageRequest.of(page, size);
        RecentLesson recentLesson = new RecentLesson();
        recentLesson.setUser(user);
        recentLesson.setLesson(lesson);
        Page<RecentLesson> recentLessonPage = new PageImpl<>(List.of(recentLesson));

        when(recentLessonRepository.findAllByUserIdOrderByLastAccessedDesc(userId, pageable)).thenReturn(recentLessonPage);

        PageDTO<RecentLessonDTO> result = recentLessonService.getRecentLessons(userId, page, size);

        assertNotNull(result);
        assertEquals(1, result.getResults().size());
        verify(recentLessonRepository, times(1)).findAllByUserIdOrderByLastAccessedDesc(userId, pageable);
    }

    @Test
    void testAddRecentLesson_UserNotFound() {
        RecentLessonRequestDTO request = new RecentLessonRequestDTO();
        request.setUserId(1);
        request.setLessonId(1);

        when(userRepository.findById(request.getUserId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            recentLessonService.addRecentLesson(request);
        });

        verify(userRepository, times(1)).findById(request.getUserId());
    }

    @Test
    void testAddRecentLesson_LessonNotFound() {
        RecentLessonRequestDTO request = new RecentLessonRequestDTO();
        request.setUserId(1);
        request.setLessonId(1);

        when(userRepository.findById(request.getUserId())).thenReturn(Optional.of(new User()));
        when(lessonRepository.findById(request.getLessonId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            recentLessonService.addRecentLesson(request);
        });

        verify(userRepository, times(1)).findById(request.getUserId());
        verify(lessonRepository, times(1)).findById(request.getLessonId());
    }

    @Test
    void testAddRecentLesson_Success() {
        RecentLessonRequestDTO request = new RecentLessonRequestDTO();
        request.setUserId(1);
        request.setLessonId(1);
//        request.setLastAccessed(LocalDateTime.now());

        Lesson lesson = new Lesson();
        User user = new User();
        user.setId(1);
        lesson.setAuthor(user);

        when(userRepository.findById(request.getUserId())).thenReturn(Optional.of(user));
        when(lessonRepository.findById(request.getLessonId())).thenReturn(Optional.of(lesson));

        RecentLesson recentLesson = new RecentLesson(null, user, lesson, request.getLastAccessed());
        when(recentLessonRepository.save(any(RecentLesson.class))).thenReturn(recentLesson);

        RecentLessonDTO result = recentLessonService.addRecentLesson(request);

        assertNotNull(result);
        verify(userRepository, times(1)).findById(request.getUserId());
        verify(lessonRepository, times(1)).findById(request.getLessonId());
        verify(recentLessonRepository, times(1)).save(any(RecentLesson.class));
    }
}
