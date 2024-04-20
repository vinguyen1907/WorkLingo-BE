package com.quizapp.worklingo.service.interfaces;

import com.quizapp.worklingo.dto.LessonDTO;
import com.quizapp.worklingo.model.Lesson;
import com.quizapp.worklingo.model.Topic;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface ITopicService {
    Page<Topic> getAllTopics(int page, int pageSize);

    Topic getTopicById(Integer id);

    Page<LessonDTO> getLessonsInTopic(Integer topicId, int page, int pageSize);

    Page<Topic> searchTopics(String query, int page, int pageSize);

    Topic createTopic(String name, String description, MultipartFile image);

    Topic updateTopic(Integer id, String name, String description, MultipartFile image);

    void deleteTopic(Integer id);
}
