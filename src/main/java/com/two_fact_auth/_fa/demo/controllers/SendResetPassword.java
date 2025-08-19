package com.two_fact_auth._fa.demo.controllers;

import com.two_fact_auth._fa.demo.dto.request.ResetPassEmailReqDto;
import com.two_fact_auth._fa.demo.services.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reset")
public class SendResetPassword {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send-link")
    public ResponseEntity<?> sendResetPassword(@RequestBody ResetPassEmailReqDto resetPassEmailReqDto) {
        try {
            emailService.sendResetPasswordEmail(resetPassEmailReqDto);
            return ResponseEntity.ok("Password reset email sent successfully.");
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send password reset email: " + e.getMessage());
        }
    }
}
