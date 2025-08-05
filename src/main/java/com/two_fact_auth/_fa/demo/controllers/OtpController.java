package com.two_fact_auth._fa.demo.controllers;

import com.two_fact_auth._fa.demo.models.OtpDetails;
import com.two_fact_auth._fa.demo.services.OtpGenerationService;
import dto.request.OtpRequestDto;
import dto.response.OtpResponseDto;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/otp")
public class OtpController {
    
    @Autowired
    private OtpGenerationService service;

        @PostMapping("/generate")
        public ResponseEntity<OtpResponseDto> generateOtp(@RequestBody OtpRequestDto otpRequestDto) throws MessagingException {
            OtpResponseDto otpResponseDto  = service.generateOtp(otpRequestDto);
            return ResponseEntity.status(HttpStatus.OK).body(otpResponseDto);
        }

        @PostMapping("/verify-otp")
        public ResponseEntity<OtpResponseDto> verifyOtp(@RequestBody OtpRequestDto otpRequestDto) {
            OtpResponseDto response = service.verifyOtp(otpRequestDto);
            HttpStatus status;
            switch(response.getOtpResponseEnum()) {
                case SUCCESS -> status = HttpStatus.OK;
                case NOT_FOUND -> status = HttpStatus.NOT_FOUND;
                case INVALID -> status = HttpStatus.UNAUTHORIZED;
                case EXPIRED -> status = HttpStatus.GONE;
                default -> status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            return new ResponseEntity<>(response, status);
        }
}
