package com.quizapp.worklingo.service;

import com.quizapp.worklingo.dto.LessonDTO;
import com.quizapp.worklingo.model.Lesson;
import com.quizapp.worklingo.model.Topic;
import com.quizapp.worklingo.repository.LessonRepository;
import com.quizapp.worklingo.repository.TopicRepository;
import com.quizapp.worklingo.service.interfaces.ITopicService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TopicService implements ITopicService {
    private final TopicRepository topicRepository;
    private final LessonRepository lessonRepository;

    @Override
    public Page<Topic> getAllTopics(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return topicRepository.findAll(pageable);
    }

    @Override
    public Topic getTopicById(Integer id) {
        return topicRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Topic not found"));
    }

    @Override
    public Page<LessonDTO> getLessonsInTopic(Integer topicId, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        var lessons = lessonRepository.findAllByTopicId(topicId, pageable);
        return lessons.map(Lesson::toDTO);
    }
}
