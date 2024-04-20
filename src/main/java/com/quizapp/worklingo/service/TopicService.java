package com.quizapp.worklingo.service;

import com.quizapp.worklingo.dto.LessonDTO;
import com.quizapp.worklingo.model.Lesson;
import com.quizapp.worklingo.model.Topic;
import com.quizapp.worklingo.repository.LessonRepository;
import com.quizapp.worklingo.repository.TopicRepository;
import com.quizapp.worklingo.service.interfaces.ICloudinaryService;
import com.quizapp.worklingo.service.interfaces.ITopicService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TopicService implements ITopicService {
    private final TopicRepository topicRepository;
    private final LessonRepository lessonRepository;
    private final ICloudinaryService cloudinaryService;

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

    @Override
    public Page<Topic> searchTopics(String query, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        var topics = topicRepository.findAllByNameContainingIgnoreCase(query, pageable);
        return topics;
    }

    @Override
    public Topic createTopic(String name, String description, MultipartFile image) {
        Topic topic = Topic.builder()
                .name(name)
                .description(description)
                .numberOfLessons(0)
                .build();
        topic = topicRepository.save(topic);

        String imageUrl = cloudinaryService.uploadTopicImage(topic.getId(), image).get("url").toString();
        topic.setImageUrl(imageUrl);

        return topicRepository.save(topic);
    }

    @Override
    public Topic updateTopic(Integer id, String name, String description, MultipartFile image) {
        System.out.println("ID: " + id);
        Topic topic = topicRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Topic not found"));
        if (name != null) {
            topic.setName(name);
        }
        if (description != null) {
            topic.setDescription(description);
        }
        if (image != null) {
            String imageUrl = cloudinaryService.uploadTopicImage(topic.getId(), image).get("url").toString();
            topic.setImageUrl(imageUrl);
        }
        return topicRepository.save(topic);
    }

    @Override
    public void deleteTopic(Integer id) {
        Topic topic = topicRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Topic not found"));
        topic.setDeleted(true);
        topicRepository.save(topic);
    }
}
