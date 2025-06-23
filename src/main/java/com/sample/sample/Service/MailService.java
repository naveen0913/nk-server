package com.sample.sample.Service;

import com.sample.sample.Model.TrackingStatus;
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


    public void sendOtpEmail(String toEmail, String otp, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Reset Your Password - WeLoveTou");
        message.setText("Hello " + username + ",\n\n"
                + "You requested to reset your password. Please use the following OTP to proceed:\n\n"
                + "🔐 OTP: " + otp + "\n\n"
                + "This OTP will expire in 10 minutes.\n\n"
                + "If you didn't request a password reset, please ignore this email.\n\n"
                + "Regards,\nWeLoveYou Team");
        mailSender.send(message);
    }

    public void sendResetSuccessMail(String toEmail,String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject(" Your Password Has Been Successfully Reset - WeLoveYou");
        message.setText("Hello " + username + ",\n\n"
                + "We wanted to let you know that your password has been successfully reset.\n\n"
                + "If you did not perform this action, please secure your account immediately by contacting support.\n\n"
                + "Thank you for using WeLoveYou.\n\n"
                + "Regards,\nWeLoveYou Team");
        mailSender.send(message);
    }

    public void sendOrderStatusMail(String toEmail, String orderNumber, String first, String last, TrackingStatus status) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);

        String fullName = (first + " " + last).trim();
        String subject;
        String messageBody;

        switch (status) {
            case ORDER_PLACED:
                subject = "🛍️ Order Placed Successfully - WeLoveYou";
                messageBody = "Hi " + fullName + ",\n\n"
                        + "Your order has been successfully placed.\n\n"
                        + "🆔 Order ID: #" + orderNumber + "\n"
                        + "📅 Order Date: " + LocalDate.now() + "\n"
                        + "📦 Status: Order Placed\n\n"
                        + "We’ll notify you once it is packed.\n";
                break;

            case PACKED:
                subject = "📦 Order Packed - WeLoveYou";
                messageBody = "Hi " + fullName + ",\n\n"
                        + "Good news! Your order has been packed and is getting ready to ship.\n\n"
                        + "🆔 Order ID: #" + orderNumber + "\n"
                        + "📦 Status: Packed\n\n"
                        + "You’ll receive another update when your order is shipped.\n";
                break;

            case SHIPPED:
                subject = "🚚 Order Shipped - WeLoveYou";
                messageBody = "Hi " + fullName + ",\n\n"
                        + "Your order has been shipped and is on the way.\n\n"
                        + "🆔 Order ID: #" + orderNumber + "\n"
                        + "📦 Status: Shipped\n\n"
                        + "Stay tuned! You’ll get a message when it’s out for delivery.\n";
                break;

            case OUT_FOR_DELIVERY:
                subject = "🛵 Out for Delivery - WeLoveYou";
                messageBody = "Hi " + fullName + ",\n\n"
                        + "Your order is out for delivery and will reach you soon.\n\n"
                        + "🆔 Order ID: #" + orderNumber + "\n"
                        + "📦 Status: Out for Delivery\n\n"
                        + "Please keep your phone nearby and ensure someone is available to receive it.\n";
                break;

            case DELIVERED:
                subject = "📬 Order Delivered - WeLoveYou";
                messageBody = "Hi " + fullName + ",\n\n"
                        + "We’re happy to let you know that your order has been delivered.\n\n"
                        + "🆔 Order ID: #" + orderNumber + "\n"
                        + "📦 Status: Delivered\n\n"
                        + "We hope you enjoy your purchase! 😊\n";
                break;

            default:
                subject = "🔔 Order Update - WeLoveYou";
                messageBody = "Hi " + fullName + ",\n\n"
                        + "There is an update regarding your order.\n\n"
                        + "🆔 Order ID: #" + orderNumber + "\n"
                        + "📦 Status: " + status + "\n\n";
        }

        messageBody += "\nThank you for choosing WeLoveYou!\n\n"
                + "Warm regards,\n"
                + "WeLoveYou Team";

        message.setSubject(subject);
        message.setText(messageBody);
        mailSender.send(message);
    }


    private void sendStatusToAdmin(String orderNumber, String customerName, TrackingStatus status, String adminMail) {
        SimpleMailMessage adminMessage = new SimpleMailMessage();
        adminMessage.setFrom(fromEmail);
        adminMessage.setTo(adminMail);

        adminMessage.setSubject("📢 Order Status Update - Admin Notification");

        String adminBody = "Admin Notification\n\n"
                + "Customer: " + customerName + "\n"
                + "Order ID: #" + orderNumber + "\n"
                + "Current Status: " + status + "\n\n"
                + "Please take necessary action if required.";

        adminMessage.setText(adminBody);
        mailSender.send(adminMessage);
    }


    public void sendPasswordChangeEmail(String toEmail, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Password Updated Successfully ");
        message.setText("Hello " + username + ",\n\nYour password has been changed successfully.\n\nIf you did not request this change, please contact support immediately.\n\nThank you.");

        mailSender.send(message);
    }



}
