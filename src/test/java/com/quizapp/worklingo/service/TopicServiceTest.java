package com.quizapp.worklingo.service;

import com.quizapp.worklingo.dto.LessonDTO;
import com.quizapp.worklingo.model.Lesson;
import com.quizapp.worklingo.model.Topic;
import com.quizapp.worklingo.model.user.User;
import com.quizapp.worklingo.repository.LessonRepository;
import com.quizapp.worklingo.repository.TopicRepository;
import com.quizapp.worklingo.service.interfaces.ICloudinaryService;
import com.quizapp.worklingo.service.interfaces.ITopicService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TopicServiceTest {

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private ICloudinaryService cloudinaryService;

    @InjectMocks
    private TopicService topicService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTopics() {
        int page = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Topic> topicPage = new PageImpl<>(Collections.emptyList());

        when(topicRepository.findAll(pageable)).thenReturn(topicPage);

        Page<Topic> result = topicService.getAllTopics(page, pageSize);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        verify(topicRepository, times(1)).findAll(pageable);
    }

    @Test
    void testGetTopicById() {
        Integer id = 1;
        Topic topic = new Topic();
        when(topicRepository.findById(id)).thenReturn(Optional.of(topic));

        Topic result = topicService.getTopicById(id);

        assertNotNull(result);
        verify(topicRepository, times(1)).findById(id);
    }

    @Test
    void testGetLessonsInTopic() {
        Integer topicId = 1;
        User user = new User();
        user.setId(1);
        int page = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(page, pageSize);
        Lesson lesson = new Lesson();
        lesson.setAuthor(user);
        Page<Lesson> lessonPage = new PageImpl<>(Collections.singletonList(lesson));

        when(lessonRepository.findAllByTopicId(topicId, pageable)).thenReturn(lessonPage);

        Page<LessonDTO> result = topicService.getLessonsInTopic(topicId, page, pageSize);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(lessonRepository, times(1)).findAllByTopicId(topicId, pageable);
    }

    @Test
    void testSearchTopics() {
        String query = "test";
        int page = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Topic> topicPage = new PageImpl<>(Collections.emptyList());

        when(topicRepository.findAllByNameContainingIgnoreCase(query, pageable)).thenReturn(topicPage);

        Page<Topic> result = topicService.searchTopics(query, page, pageSize);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        verify(topicRepository, times(1)).findAllByNameContainingIgnoreCase(query, pageable);
    }

    // You can write similar tests for createTopic, updateTopic, and deleteTopic methods
}
