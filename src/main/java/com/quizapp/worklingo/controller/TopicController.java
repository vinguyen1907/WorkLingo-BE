package com.quizapp.worklingo.controller;

import com.quizapp.worklingo.dto.LessonDTO;
import com.quizapp.worklingo.dto.PageDTO;
import com.quizapp.worklingo.model.Lesson;
import com.quizapp.worklingo.model.Topic;
import com.quizapp.worklingo.service.interfaces.ITopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/topics")
@RequiredArgsConstructor
@Tag(name = "Topics")
public class TopicController {
    private final ITopicService topicService;

    @GetMapping()
    @Operation(summary = "This method is used to get all topics.")
    public ResponseEntity<PageDTO<Topic>> getAllTopics(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return ResponseEntity.ok(new PageDTO<>(topicService.getAllTopics(page, size)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "This method is used to get a topic by id.")
    public ResponseEntity<Topic> getTopicById(
            @PathVariable Integer id
    ) {
         return ResponseEntity.ok(topicService.getTopicById(id));
    }

    @GetMapping("/{topicId}/lessons")
    @Operation(summary = "This method is used to get lessons in a topic.")
    public ResponseEntity<PageDTO<LessonDTO>> getLessonsInTopic(
            @PathVariable Integer topicId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(new PageDTO<>(topicService.getLessonsInTopic(topicId,page, size)));
    }

    @GetMapping("/search")
    @Operation(summary = "Search topics by name.")
    public ResponseEntity<PageDTO<Topic>> searchLessons(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(new PageDTO<>(topicService.searchTopics(query, page, size)));
    }

    @PostMapping()
    @Operation(summary = "Create a new topic.")
    public ResponseEntity<Topic> createTopic(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam MultipartFile image
            ) {
        return ResponseEntity.ok(topicService.createTopic(name, description, image));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update a topic.")
    public ResponseEntity<Topic> updateTopic(
            @PathVariable Integer id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) MultipartFile image
    ) {
        return ResponseEntity.ok(topicService.updateTopic(id, name, description, image));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a topic.")
    public ResponseEntity<Void> deleteTopic(
            @PathVariable Integer id
    ) {
        topicService.deleteTopic(id);
        return ResponseEntity.noContent().build();
    }
}
