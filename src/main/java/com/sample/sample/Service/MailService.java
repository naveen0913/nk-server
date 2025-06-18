package com.sample.sample.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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

    public void sendOrderPlacedMail(String toEmail, String orderNumber) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("🎉 Order Confirmation - WeLoveYou");

        message.setText("Hello,\n\n"
                + "Thank you for shopping with us at WeLoveYou!\n\n"
                + "We're excited to let you know that your order has been successfully placed.\n\n"
                + "🛒 Order Details:\n"
                + "- Order Number: #" + orderNumber + "\n"
                + "- Order Date: " + LocalDate.now() + "\n\n"
                + "- Order Status: Placed \n\n"
                + "We'll send you another update once your items are shipped.\n\n"
                + "If you have any questions, feel free to reach out to our support team.\n\n"
                + "Thank you for choosing WeLoveYou. We truly appreciate your business!\n\n"
                + "Warm regards,\n"
                + "WeLoveYou Team");

        mailSender.send(message);
    }



}
