package com.quizapp.worklingo.service.interfaces;

import com.quizapp.worklingo.dto.FavoritesDTO;
import com.quizapp.worklingo.dto.LessonDTO;
import com.quizapp.worklingo.dto.PageDTO;

public interface IFavoritesService {
    PageDTO<LessonDTO> getFavoriteLessons(Integer userId, int page, int size);
    FavoritesDTO addFavoriteLesson(Integer userId, Integer lessonId);
    void removeFavoriteLesson(Integer userId, Integer lessonId);
}
