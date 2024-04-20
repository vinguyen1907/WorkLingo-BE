package com.quizapp.worklingo.service;

import com.cloudinary.Cloudinary;
import com.quizapp.worklingo.service.interfaces.ICloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService implements ICloudinaryService {
    private final Cloudinary cloudinary;

    public Map uploadUserAvatar(Integer userId, MultipartFile image) {
        try {
            Map<String, String> options = new HashMap<>();
            options.put("public_id", String.valueOf(userId));
            options.put("folder", "worklingo/users/avatar/");
            return cloudinary.uploader().upload(image.getBytes(), options);
        } catch (IOException e) {
            throw new RuntimeException("Error while uploading image -- " + e.getMessage());
        }

    }

    public Map uploadTopicImage(Integer topicId, MultipartFile image) {
        try {
            Map<String, String> options = new HashMap<>();
            options.put("public_id", "topic_" + topicId);
            options.put("folder", "worklingo/topics/");
            return cloudinary.uploader().upload(image.getBytes(), options);
        } catch (IOException e) {
            throw new RuntimeException("Error while uploading image -- " + e.getMessage());
        }
    }
}
