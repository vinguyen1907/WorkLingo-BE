package com.quizapp.worklingo.service.interfaces;

import com.quizapp.worklingo.dto.FlashcardDTO;
import com.quizapp.worklingo.dto.LessonDTO;
import com.quizapp.worklingo.dto.PageDTO;
import com.quizapp.worklingo.dto.RecentLessonDTO;

public interface ILessonService {
    PageDTO<LessonDTO> getTopRatingLessons(int page, int size);

    LessonDTO getLessonById(Integer id);

    PageDTO<FlashcardDTO> getFlashcardsInLesson(Integer lessonId, int page, int size);

    PageDTO<RecentLessonDTO> getRecentLessons(Integer userId, int page, int size);
}
