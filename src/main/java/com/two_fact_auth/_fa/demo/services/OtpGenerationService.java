package com.two_fact_auth._fa.demo.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.two_fact_auth._fa.demo.enums.OtpResponseEnum;
import com.two_fact_auth._fa.demo.models.OtpDetails;
import com.two_fact_auth._fa.demo.dto.request.OtpRequestDto;
import com.two_fact_auth._fa.demo.dto.response.OtpResponseDto;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class OtpGenerationService {

    @Autowired
    private RedisTemplate<String, OtpDetails> redisTemplate;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private EmailService emailService;

    private static final long OTP_EXPIRE_TIME = 120; //seconds

    private static final Logger logger = LoggerFactory.getLogger(OtpGenerationService.class);

    public OtpResponseDto generateOtp(OtpRequestDto otpRequestDto) throws MessagingException {
        String email = otpRequestDto.getEmail();
        String otp = String.format("%06d", new Random().nextInt(999999));

        OtpDetails otpDetails = new OtpDetails();
        otpDetails.setEmail(email);
        otpDetails.setOtp(otp);
        otpDetails.setExpiryTime(LocalDateTime.now().plusSeconds(OTP_EXPIRE_TIME));

        redisTemplate.opsForValue().set(email, otpDetails, OTP_EXPIRE_TIME, TimeUnit.SECONDS);

        //send email
        logger.info("Email sent to email {}", email);
        emailService.sendEmailOtp(email, otp);

        OtpResponseDto otpResponseDto = new OtpResponseDto();
        otpResponseDto.setOtp_response("Otp Sent To email");
        return otpResponseDto;
    }

    public OtpResponseDto verifyOtp(OtpRequestDto otpRequestDto) {
        String email = otpRequestDto.getEmail();
        String submittedOtp = otpRequestDto.getOtp();

        Object obj = redisTemplate.opsForValue().get(email);
        OtpDetails storedOtpDetails = mapper.convertValue(obj, OtpDetails.class);

        if(storedOtpDetails == null) {
            return new OtpResponseDto("Otp not found", OtpResponseEnum.NOT_FOUND);
        }

        if(!storedOtpDetails.getOtp().equals(submittedOtp)) {
            return new OtpResponseDto("Invalid OTP", OtpResponseEnum.INVALID);
        }

        if (storedOtpDetails.getExpiryTime().isBefore(LocalDateTime.now())) {
            redisTemplate.delete(email);
            return new OtpResponseDto("Otp Expired", OtpResponseEnum.EXPIRED);
        }

        //all clear
        redisTemplate.delete(email);
        return new OtpResponseDto("OTP Verification Successful", OtpResponseEnum.SUCCESS);
    }
}
