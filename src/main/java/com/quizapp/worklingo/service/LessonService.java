package com.quizapp.worklingo.service;

import com.quizapp.worklingo.dto.FlashcardDTO;
import com.quizapp.worklingo.dto.LessonDTO;
import com.quizapp.worklingo.dto.PageDTO;
import com.quizapp.worklingo.dto.RecentLessonDTO;
import com.quizapp.worklingo.model.Flashcard;
import com.quizapp.worklingo.model.Lesson;
import com.quizapp.worklingo.model.RecentLesson;
import com.quizapp.worklingo.repository.FlashcardRepository;
import com.quizapp.worklingo.repository.LessonRepository;
import com.quizapp.worklingo.repository.RecentLessonRepository;
import com.quizapp.worklingo.service.interfaces.ILessonService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LessonService implements ILessonService {
    private  final LessonRepository lessonRepository;
    private  final FlashcardRepository flashcardRepository;
    private final RecentLessonRepository recentLessonRepository;

    @Override
    public PageDTO<LessonDTO> getTopRatingLessons(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return new PageDTO<>(lessonRepository.findAllByOrderByNumberOfUpVotesDesc(pageable).map(Lesson::toDTO));
    }

    @Override
    public LessonDTO getLessonById(Integer id) {
        return lessonRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Lesson not found")).toDTO();
    }

    @Override
    public PageDTO<FlashcardDTO> getFlashcardsInLesson(Integer lessonId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return new PageDTO<>(flashcardRepository.findAllByLessonId(lessonId, pageable).map(Flashcard::toDTO));
    }

    @Override
    public PageDTO<RecentLessonDTO> getRecentLessons(Integer userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return new PageDTO<>(recentLessonRepository.findAllByUserIdOrderByLastAccessedDesc(userId, pageable).map(RecentLesson::toDTO));
    }
}
