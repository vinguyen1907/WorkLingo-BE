package com.quizapp.worklingo.repository;

import com.quizapp.worklingo.model.OTP;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpRepository extends JpaRepository<OTP, Integer> {
    OTP findByUserId(Integer userId);
}
