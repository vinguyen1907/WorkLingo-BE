package com.quizapp.worklingo.dto;

import com.quizapp.worklingo.model.user.Role;
import com.quizapp.worklingo.model.user.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String jobTitle;
    private String avatarUrl;
    private String bio;

    @Enumerated(EnumType.STRING)
    private Role role;

    public UserDTO(User user) {
        this.id = user.getId();
        this.firstname = user.getFirstname();
        this.lastname = user.getLastname();
        this.email = user.getEmail();
        this.jobTitle = user.getJobTitle();
        this.avatarUrl = user.getAvatarUrl();
        this.bio = user.getBio();
        this.role = user.getRole();
    }
}
