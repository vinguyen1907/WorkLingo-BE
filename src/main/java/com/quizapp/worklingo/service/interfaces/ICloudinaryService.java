package com.quizapp.worklingo.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface ICloudinaryService {
    Map uploadUserAvatar(Integer userId, MultipartFile image);
}
