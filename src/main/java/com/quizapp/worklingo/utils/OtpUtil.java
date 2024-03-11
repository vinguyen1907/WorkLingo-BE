package com.quizapp.worklingo.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class OtpUtil {
    public String generateOtp() {
        Random random = new Random();
        int otp = random.nextInt(999999);
        return String.format("%06d", otp);
    }
}
