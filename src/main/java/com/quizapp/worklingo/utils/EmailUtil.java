package com.quizapp.worklingo.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailUtil {
    private final JavaMailSender emailSender;

    public void sendEmail(String email, String subject, String content) throws MailException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no_reply@example.com");
        message.setTo(email);
        message.setSubject(subject);

        message.setText(content);

        emailSender.send(message);
    }

    public void sendForgotPasswordEmail(
            String email, String code
    ) {
        sendEmail(
                email,
                "WorkLingo - Reset password email",
                "You're receiving this e-mail because you or someone else has requested a password reset for your user account at .\n" +
                        "\n" +
                        "Use the code below to reset your password:\n" +
                        code + "\n" +
                        "\n" +
                        "If you d id not request a password reset you can safely ignore this email."
                );
    }
}
