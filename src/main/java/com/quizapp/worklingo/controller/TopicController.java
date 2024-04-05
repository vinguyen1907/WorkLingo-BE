package com.quizapp.worklingo.controller;

import com.quizapp.worklingo.model.Lesson;
import com.quizapp.worklingo.model.Topic;
import com.quizapp.worklingo.service.interfaces.ITopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1/topics")
@RequiredArgsConstructor
public class TopicController {
    private final ITopicService topicService;

    @GetMapping()
    public ResponseEntity<Page<Topic>> getAllTopics(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return ResponseEntity.ok(topicService.getAllTopics(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Topic> getTopicById(
            @PathVariable Integer id
    ) {
         return ResponseEntity.ok(topicService.getTopicById(id));
    }

    @GetMapping("/{topicId}/lessons")
    public ResponseEntity<Page<Lesson>> getLessonsInTopic(
            @PathVariable Integer topicId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(topicService.getLessonsInTopic(topicId,page, size));
    }
}
