package com.two_fact_auth._fa.demo.controllers;

import com.two_fact_auth._fa.demo.services.OtpGenerationService;
import com.two_fact_auth._fa.demo.dto.request.OtpRequestDto;
import com.two_fact_auth._fa.demo.dto.response.OtpResponseDto;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            System.out.println("lkjdfouipkljophophdfgdfg " + response);
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
