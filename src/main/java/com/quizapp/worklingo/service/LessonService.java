package com.quizapp.worklingo.service;

import com.quizapp.worklingo.dto.FlashcardDTO;
import com.quizapp.worklingo.dto.LessonDTO;
import com.quizapp.worklingo.dto.PageDTO;
import com.quizapp.worklingo.dto.RecentLessonDTO;
import com.quizapp.worklingo.dto.request.CreateLessonRequest;
import com.quizapp.worklingo.dto.request.UpdateLessonRequest;
import com.quizapp.worklingo.enums.LessonVisibility;
import com.quizapp.worklingo.model.Flashcard;
import com.quizapp.worklingo.model.Lesson;
import com.quizapp.worklingo.model.RecentLesson;
import com.quizapp.worklingo.model.Topic;
import com.quizapp.worklingo.model.user.User;
import com.quizapp.worklingo.repository.*;
import com.quizapp.worklingo.service.interfaces.ILessonService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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
        System.out.println("Topic id: " + request.getTopicId());
        Topic topic = topicRepository.findById(request.getTopicId()).orElseThrow(() -> new EntityNotFoundException("Topic not found"));
        User user = userRepository.findById(request.getAuthorId()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        Lesson lesson = Lesson.builder()
                .title(request.getTitle())
                .topic(topic)
                .author(user)
                .numberOfUpVotes(0)
                .numberOfDownVotes(0)
                .numberOfFlashcards(request.getFlashcards().size())
                .flashcards(request.getFlashcards())
                .visibility(request.getVisibility() == null ? LessonVisibility.PUBLIC : request.getVisibility())
                .createdTime(LocalDateTime.now())
                .updatedTime(LocalDateTime.now())
                .isDeleted(false)
                .build();

        increaseNumberOfLessonsInTopic(topic);

        return lessonRepository.save(lesson).toDTO();
    }

    private void increaseNumberOfLessonsInTopic(Topic topic) {
        topic.setNumberOfLessons(topic.getNumberOfLessons() + 1);
        topicRepository.save(topic);
    }

    @Override
    public LessonDTO updateLesson(Integer lessonId, UpdateLessonRequest request) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new EntityNotFoundException("Lesson not found"));
        lesson.setTitle(request.getTitle());
        lesson.setVisibility(request.getVisibility());
        return lessonRepository.save(lesson).toDTO();
    }

    @Override
    public void deleteLesson(Integer lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new EntityNotFoundException("Lesson not found"));
        lessonRepository.delete(lesson);
    }

    @Override
    @Transactional
    public void deleteFlashcardInLesson(Integer lessonId, Integer flashcardId) {
        Flashcard flashcard = flashcardRepository.findById(flashcardId).orElseThrow(() -> new EntityNotFoundException("Flashcard not found"));
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new EntityNotFoundException("Lesson not found"));
        if (!flashcard.getLesson().getId().equals(lessonId)) {
            throw new IllegalArgumentException("Flashcard does not belong to lesson");
        }
        flashcardRepository.delete(flashcard);

        // Update number of flashcards in lesson
        lesson.setNumberOfFlashcards(lesson.getNumberOfFlashcards() - 1);
        lessonRepository.save(lesson);
    }

    @Override
    public PageDTO<LessonDTO> getOwnLessons(Integer userId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return new PageDTO<>(lessonRepository.findAllByAuthorIdOrderByUpdatedTimeDesc(userId, pageable).map(Lesson::toDTO));
    }

    @Override
    public PageDTO<LessonDTO> getAllLessons(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return new PageDTO<>(lessonRepository.findAll(pageable).map(Lesson::toDTO));
    }
}
