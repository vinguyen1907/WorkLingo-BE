package com.quizapp.worklingo.repository;

import com.quizapp.worklingo.model.Favorites;
import com.quizapp.worklingo.model.FavoritesId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoritesRepository extends JpaRepository<Favorites, FavoritesId> {
    Page<Favorites> findAllByUserId(Integer userId, Pageable pageable);
    Optional<Favorites> findAllByUserIdAndLessonId(Integer userId, Integer lessonId);
}
