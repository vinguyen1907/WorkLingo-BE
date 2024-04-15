package com.quizapp.worklingo.service;

import com.quizapp.worklingo.dto.FavoritesDTO;
import com.quizapp.worklingo.dto.LessonDTO;
import com.quizapp.worklingo.dto.PageDTO;
import com.quizapp.worklingo.model.Favorites;
import com.quizapp.worklingo.model.FavoritesId;
import com.quizapp.worklingo.repository.FavoritesRepository;
import com.quizapp.worklingo.repository.LessonRepository;
import com.quizapp.worklingo.repository.UserRepository;
import com.quizapp.worklingo.service.interfaces.IFavoritesService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoritesService implements IFavoritesService {
    private final FavoritesRepository favoritesRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;

    @Override
    public PageDTO<LessonDTO> getFavoriteLessons(Integer userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Favorites> favorites = favoritesRepository.findAllByUserId(userId, pageable);
        return new PageDTO<>(favorites.map(favorite -> new LessonDTO(favorite.getLesson())));
    }

    @Override
    public FavoritesDTO addFavoriteLesson(Integer userId, Integer lessonId) {
        var user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        var lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new EntityNotFoundException("Lesson not found"));
        var favorite = new Favorites(
                new FavoritesId(
                    user.getId(),
                    lesson.getId()
                ),
                user,
                lesson
        );
        return favoritesRepository.save(favorite).toDTO();
    }

    @Override
    public void removeFavoriteLesson(Integer userId, Integer lessonId) {
        var favorite = favoritesRepository.findAllByUserIdAndLessonId(userId, lessonId)
                .orElseThrow(() -> new EntityNotFoundException("Favorite not found"));
        favoritesRepository.delete(favorite);
    }
}
