package com.quizapp.worklingo.service.interfaces;

import com.quizapp.worklingo.dto.FlashcardDTO;
import com.quizapp.worklingo.dto.LessonDTO;
import com.quizapp.worklingo.dto.PageDTO;
import com.quizapp.worklingo.dto.RecentLessonDTO;
import com.quizapp.worklingo.dto.request.CreateLessonRequest;

import java.util.List;

public interface ILessonService {
    PageDTO<LessonDTO> getTopRatingLessons(int page, int size);

    LessonDTO getLessonById(Integer id);

    List<FlashcardDTO> getFlashcardsInLesson(Integer lessonId);

    PageDTO<RecentLessonDTO> getRecentLessons(Integer userId, int page, int size);
    LessonDTO createLesson(CreateLessonRequest request);
}
