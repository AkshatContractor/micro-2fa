package com.two_fact_auth._fa.demo.services;

import com.two_fact_auth._fa.demo.dto.request.ResetPassEmailReqDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private JavaMailSenderImpl mailSender;

    public void sendEmailOtp(String toEmail, String Otp) throws MessagingException {
        MimeMessage message =   mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject("Your One Time Password");
        helper.setText(
                """
                <html>
                  <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;">
                    <table style="max-width: 500px; margin: auto; background-color: #ffffff; border-radius: 8px; padding: 30px; box-shadow: 0 4px 8px rgba(0,0,0,0.1);">
                      <tr>
                        <td style="text-align: center;">
                          <h2 style="color: #333333;">🔐 Two-Factor Authentication</h2>
                          <p style="font-size: 16px; color: #555555;">Use the following One-Time Password (OTP) to complete your authentication:</p>
                          <div style="margin: 20px 0; font-size: 28px; font-weight: bold; color: #ffffff; background-color: #4CAF50; padding: 10px 20px; border-radius: 6px; display: inline-block;">
                            %s
                          </div>
                          <p style="font-size: 14px; color: #888888;">This OTP is valid for <b>3 minutes</b>. Do not share it with anyone.</p>
                          <hr style="margin: 30px 0; border: none; border-top: 1px solid #eeeeee;">
                          <p style="font-size: 12px; color: #aaaaaa;">If you did not request this, please ignore this email.</p>
                        </td>
                      </tr>
                    </table>
                  </body>
                </html>
                """.formatted(Otp),
                true
        );
        mailSender.send(message);
    }

    public void sendResetPasswordEmail(ResetPassEmailReqDto resetPassEmailReqDto) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        String toEmail = resetPassEmailReqDto.getEmail();
        String resetLink = resetPassEmailReqDto.getResetLink();

        helper.setTo(toEmail);
        helper.setSubject("Password Reset Request");
        helper.setText(
                """
                <html>
                  <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;">
                    <table style="max-width: 500px; margin: auto; background-color: #ffffff; border-radius: 8px; padding: 30px; box-shadow: 0 4px 8px rgba(0,0,0,0.1);">
                      <tr>
                        <td style="text-align: center;">
                          <h2 style="color: #333333;">🔐 Password Reset</h2>
                          <p style="font-size: 16px; color: #555555;">You have requested a password reset. Click the link below to set a new password:</p>
                          <div style="margin: 20px 0;">
                            <a href="%s" style="font-size: 18px; font-weight: bold; color: #ffffff; background-color: #4CAF50; padding: 12px 24px; border-radius: 6px; text-decoration: none; display: inline-block;">
                              Reset Your Password
                            </a>
                          </div>
                          <p style="font-size: 14px; color: #888888;">This link is valid for <b>10 minutes</b>. If you did not request a password reset, please ignore this email.</p>
                          <hr style="margin: 30px 0; border: none; border-top: 1px solid #eeeeee;">
                          <p style="font-size: 12px; color: #aaaaaa;">If you have any issues, please contact our support team.</p>
                        </td>
                      </tr>
                    </table>
                  </body>
                </html>
                """.formatted(resetLink),
                true
        );
        mailSender.send(message);
    }
}
