package com.quizapp.worklingo.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateUserProfileRequest {
    private String firstName;
    private String lastName;
    private String jobTitle;
    private String bio;
}
