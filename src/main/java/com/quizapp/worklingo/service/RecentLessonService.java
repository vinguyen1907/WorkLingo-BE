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
import com.quizapp.worklingo.service.interfaces.IRecentLessonService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecentLessonService implements IRecentLessonService {
    private final RecentLessonRepository recentLessonRepository;
    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;

    @Override
    public PageDTO<RecentLessonDTO> getRecentLessons(Integer userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return new PageDTO<>(recentLessonRepository.findAllByUserIdOrderByLastAccessedDesc(userId, pageable).map(RecentLesson::toDTO));
    }

    @Override
    public RecentLessonDTO addRecentLesson(RecentLessonRequestDTO request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        Lesson lesson = lessonRepository.findById(request.getUserId()).orElseThrow(() -> new EntityNotFoundException("Lesson not found"));
        RecentLesson recentLesson = new RecentLesson(
                null,
                user,
                lesson,
                request.getLastAccessed()
        );
        return recentLessonRepository.save(recentLesson).toDTO();
    }
}
