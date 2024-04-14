package com.quizapp.worklingo.service.interfaces;

import com.quizapp.worklingo.dto.PageDTO;
import com.quizapp.worklingo.dto.RecentLessonDTO;
import com.quizapp.worklingo.dto.request.RecentLessonRequestDTO;

public interface IRecentLessonService {
    PageDTO<RecentLessonDTO> getRecentLessons(Integer userId, int page, int size);

    RecentLessonDTO addRecentLesson(RecentLessonRequestDTO request);
}
