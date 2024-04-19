package com.quizapp.worklingo.service.interfaces;

import com.quizapp.worklingo.dto.LessonDTO;
import com.quizapp.worklingo.model.Lesson;
import com.quizapp.worklingo.model.Topic;
import org.springframework.data.domain.Page;

public interface ITopicService {
    Page<Topic> getAllTopics(int page, int pageSize);
    Topic getTopicById(Integer id);
    Page<LessonDTO> getLessonsInTopic(Integer topicId, int page, int pageSize);
    Page<Topic> searchTopics(String query, int page, int pageSize);
}
