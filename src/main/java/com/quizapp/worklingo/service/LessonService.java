package com.quizapp.worklingo.service;

import com.quizapp.worklingo.dto.FlashcardDTO;
import com.quizapp.worklingo.dto.LessonDTO;
import com.quizapp.worklingo.dto.PageDTO;
import com.quizapp.worklingo.dto.RecentLessonDTO;
import com.quizapp.worklingo.dto.request.CreateLessonRequest;
import com.quizapp.worklingo.enums.LessonVisibility;
import com.quizapp.worklingo.model.Flashcard;
import com.quizapp.worklingo.model.Lesson;
import com.quizapp.worklingo.model.RecentLesson;
import com.quizapp.worklingo.model.Topic;
import com.quizapp.worklingo.model.user.User;
import com.quizapp.worklingo.repository.*;
import com.quizapp.worklingo.service.interfaces.ILessonService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService implements ILessonService {
    private  final LessonRepository lessonRepository;
    private  final FlashcardRepository flashcardRepository;
    private final RecentLessonRepository recentLessonRepository;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;

    @Override
    public PageDTO<LessonDTO> getTopRatingLessons(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return new PageDTO<>(lessonRepository.findAllByVisibilityOrderByNumberOfUpVotesDesc(LessonVisibility.PUBLIC, pageable).map(Lesson::toDTO));
    }

    @Override
    public LessonDTO getLessonById(Integer id) {
        return lessonRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Lesson not found")).toDTO();
    }

    @Override
    public List<FlashcardDTO> getFlashcardsInLesson(Integer lessonId) {
        return flashcardRepository.findAllByLessonId(lessonId).stream().map(Flashcard::toDTO).toList();
    }

    @Override
    public PageDTO<RecentLessonDTO> getRecentLessons(Integer userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return new PageDTO<>(recentLessonRepository.findAllByUserIdOrderByLastAccessedDesc(userId, pageable).map(RecentLesson::toDTO));
    }

    @Override
    public LessonDTO createLesson(CreateLessonRequest request) {
        Topic topic = topicRepository.findById(request.getTopicId()).orElseThrow(() -> new EntityNotFoundException("Topic not found"));
        User user = userRepository.findById(request.getAuthorId()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        Lesson lesson = new Lesson(
                null,
                request.getTitle(),
                topic,
                user,
                0,
                0,
                request.getFlashcards().size(),
                request.getFlashcards(),
                request.getVisibility() == null ? LessonVisibility.PUBLIC : request.getVisibility(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        return lessonRepository.save(lesson).toDTO();
    }
}
