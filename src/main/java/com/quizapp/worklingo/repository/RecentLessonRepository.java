package com.quizapp.worklingo.repository;

import com.quizapp.worklingo.model.Lesson;
import com.quizapp.worklingo.model.RecentLesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecentLessonRepository extends JpaRepository<RecentLesson, Integer> {
    Page<RecentLesson> findAllByUserIdOrderByLastAccessedDesc(Integer userId,Pageable pageable);
}
