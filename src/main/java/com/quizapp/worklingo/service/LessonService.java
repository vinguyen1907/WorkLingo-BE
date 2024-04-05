package com.quizapp.worklingo.service;

import com.quizapp.worklingo.model.Lesson;
import com.quizapp.worklingo.repository.LessonRepository;
import com.quizapp.worklingo.service.interfaces.ILessonService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LessonService implements ILessonService {
    private  final LessonRepository lessonRepository;

    @Override
    public Lesson getLessonById(Integer id) {
        return lessonRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Lesson not found"));
    }
}
