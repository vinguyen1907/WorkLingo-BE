package com.quizapp.worklingo.service;

import com.quizapp.worklingo.dto.FavoritesDTO;
import com.quizapp.worklingo.dto.LessonDTO;
import com.quizapp.worklingo.dto.PageDTO;
import com.quizapp.worklingo.model.Favorites;
import com.quizapp.worklingo.model.FavoritesId;
import com.quizapp.worklingo.model.Lesson;
import com.quizapp.worklingo.model.user.User;
import com.quizapp.worklingo.repository.FavoritesRepository;
import com.quizapp.worklingo.repository.LessonRepository;
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

public class FavoriteServiceTest {

    @Mock
    private FavoritesRepository favoritesRepository;

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FavoritesService favoritesService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetFavoriteLessons() {
        int userId = 1;
        int page = 0;
        int size = 5;
        Pageable pageable = PageRequest.of(page, size);

        User user = new User();
        user.setId(userId);

        Lesson lesson = new Lesson();
        lesson.setId(1);
        lesson.setAuthor(user);

        Favorites favorite = new Favorites(new FavoritesId(userId, 1), user, lesson);
        Page<Favorites> favoritesPage = new PageImpl<>(List.of(favorite));

        when(favoritesRepository.findAllByUserId(userId, pageable)).thenReturn(favoritesPage);

        PageDTO<LessonDTO> result = favoritesService.getFavoriteLessons(userId, page, size);

        assertNotNull(result);
        assertEquals(1, result.getResults().size());
        verify(favoritesRepository, times(1)).findAllByUserId(userId, pageable);
    }

    @Test
    public void testAddFavoriteLesson() {
        int userId = 1;
        int lessonId = 1;

        User user = new User();
        user.setId(userId);
        Lesson lesson = new Lesson();
        lesson.setId(lessonId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));
        when(favoritesRepository.save(any(Favorites.class))).thenAnswer(invocation -> invocation.getArgument(0));

        FavoritesDTO result = favoritesService.addFavoriteLesson(userId, lessonId);

        assertNotNull(result);
        verify(userRepository, times(1)).findById(userId);
        verify(lessonRepository, times(1)).findById(lessonId);
        verify(favoritesRepository, times(1)).save(any(Favorites.class));
    }

    @Test
    public void testRemoveFavoriteLesson() {
        int userId = 1;
        int lessonId = 1;

        User user = new User();
        user.setId(userId);
        Lesson lesson = new Lesson();
        lesson.setId(lessonId);

        Favorites favorite = new Favorites(new FavoritesId(userId, lessonId), user, lesson);

        when(favoritesRepository.findAllByUserIdAndLessonId(userId, lessonId)).thenReturn(Optional.of(favorite));

        favoritesService.removeFavoriteLesson(userId, lessonId);

        verify(favoritesRepository, times(1)).findAllByUserIdAndLessonId(userId, lessonId);
        verify(favoritesRepository, times(1)).delete(favorite);
    }

    @Test
    public void testAddFavoriteLesson_UserNotFound() {
        int userId = 1;
        int lessonId = 1;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            favoritesService.addFavoriteLesson(userId, lessonId);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void testAddFavoriteLesson_LessonNotFound() {
        int userId = 1;
        int lessonId = 1;

        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            favoritesService.addFavoriteLesson(userId, lessonId);
        });

        assertEquals("Lesson not found", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(lessonRepository, times(1)).findById(lessonId);
    }

    @Test
    public void testRemoveFavoriteLesson_FavoriteNotFound() {
        int userId = 1;
        int lessonId = 1;

        when(favoritesRepository.findAllByUserIdAndLessonId(userId, lessonId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            favoritesService.removeFavoriteLesson(userId, lessonId);
        });

        assertEquals("Favorite not found", exception.getMessage());
        verify(favoritesRepository, times(1)).findAllByUserIdAndLessonId(userId, lessonId);
    }
}
