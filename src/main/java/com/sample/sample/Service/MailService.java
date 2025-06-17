package com.sample.sample.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    JavaMailSender mailSender;

    @Value("{spring.mail.username}")
    private String fromEmail;


    public void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Reset Your Password - WeLoveTou");
        message.setText("Hello,\n\n"
                + "You requested to reset your password. Please use the following OTP to proceed:\n\n"
                + "🔐 OTP: " + otp + "\n\n"
                + "This OTP will expire in 10 minutes.\n\n"
                + "If you didn't request a password reset, please ignore this email.\n\n"
                + "Regards,\nWeLoveYou Team");
        mailSender.send(message);
    }

    public void sendResetSuccessMail(String toEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject(" Your Password Has Been Successfully Reset - WeLoveYou");
        message.setText("Hello,\n\n"
                + "We wanted to let you know that your password has been successfully reset.\n\n"
                + "If you did not perform this action, please secure your account immediately by contacting support.\n\n"
                + "Thank you for using WeLoveYou.\n\n"
                + "Regards,\nWeLoveYou Team");
        mailSender.send(message);
    }


}
