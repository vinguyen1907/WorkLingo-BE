package com.quizapp.worklingo.service.interfaces;

import com.quizapp.worklingo.model.Lesson;
import com.quizapp.worklingo.model.Topic;
import org.springframework.data.domain.Page;

public interface ITopicService {
    Page<Topic> getAllTopics(int page, int pageSize);
    Topic getTopicById(Integer id);
    Page<Lesson> getLessonsInTopic(Integer topicId, int page, int pageSize);
}
