package com.sample.sample.Service;

import com.sample.sample.Model.TrackingStatus;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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
            case CREATED:
                subject = "🛍️ Order Placed Successfully - WeLoveYou";
                messageBody = "Hi " + fullName + ",\n\n"
                        + "Your order has been successfully placed.\n\n"
                        + "🆔 Order ID: #" + orderNumber + "\n"
                        + "📅 Order Date: " + LocalDate.now() + "\n"
                        + "📦 Status: Order Placed\n\n"
                        + "We’ll notify you once it is packed.\n";
                break;



            case SHIPPED:
                subject = "🚚 Order Shipped - WeLoveYou";
                messageBody = "Hi " + fullName + ",\n\n"
                        + "Your order has been shipped and is on the way.\n\n"
                        + "🆔 Order ID: #" + orderNumber + "\n"
                        + "📦 Status: Shipped\n\n"
                        + "Stay tuned! You’ll get a message when it’s out for delivery.\n";
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


    public void sendAccountCreationMail(String toEmail, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail); // sender email configured in application.properties
        message.setTo(toEmail);
        message.setSubject("Your account has been created successfully - WeLoveYou");

        String body = "Hello " + username + ",\n\n"
                + "Welcome to WeLoveYou! Your account has been created successfully.\n\n"
                + "Start customizing your dream product today.\n\n"
                + "Warm regards,\nWeLoveYou Team";

        message.setText(body);
        mailSender.send(message);
    }


    public void sendLoginMail(String toEmail, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Login Alert - WeLoveYou");

        String body = "Hello " + username + ",\n\n"
                + "You have successfully logged in to your WeLoveYou account.\n\n"
                + "Warm regards,\nWeLoveYou Team";

        message.setText(body);
        mailSender.send(message);
    }


    public void sendAddressSavedMail(String toEmail, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Address Updated - WeLoveYou");

        String body = "Hi " + username + ",\n\n"
                + "Your address has been saved successfully! We're now ready to bring joy to your doorstep.\n\n"
                + "Warm regards,\n"
                + "The WeLoveYou Team";

        message.setText(body);
        mailSender.send(message);
    }


    public void sendAddressUpdatedMail(String toEmail, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Address Updated - WeLoveYou");

        String body = "Hi " + username + ",\n\n"
                + "Your address has been updated successfully! Update received! We're ready to ship your next joy-filled package.\n\n"
                + "Warm regards,\n"
                + "The WeLoveYou Team";

        message.setText(body);
        mailSender.send(message);
    }


    public void sendCartItemAddedMail(String toEmail, String username, String productName, int quantity) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Great News! Item Added to Your Cart 🎉 -- WeLoveYou");

        String body = "Hey " + username + ",\n\n"
                + "Great news! Your cart now includes:\n\n"
                + quantity + " " + productName + (quantity > 1 ? "s" : "") + "\n"
                + "Can’t wait to see how you customize them!\n\n"
                + "Cheers,\n"
                + "The WeLoveYou Team";

        message.setText(body);
        mailSender.send(message);
    }


    public void sendCartItemUpdatedMail(String toEmail, String username, String productName, int quantity) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Your Cart Was Just Updated -- WeLoveYou");

        String body = "Hi " + username + ",\n\n"
                + "Your cart has been successfully updated with the following:\n"
                + productName + " — " + quantity + " unit(s)\n\n"
                + "Thanks for continuing your design journey with WeLoveYou.\n\n"
                + "Warm regards,\n"
                + "The WeLoveYou Team";

        message.setText(body);
        mailSender.send(message);
    }


    public void sendCartItemDeletedMail(String toEmail, String username, String productName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Item Removed from Your Cart -- WeLoveYou");

        String body = "Hi " + username + ",\n\n"
                + "You’ve just removed the following item from your cart:\n"
                + productName + "\n\n"
                + "If this was a mistake, you can always add it back anytime.\n\n"
                + "Warm regards,\n"
                + "The WeLoveYou Team";

        message.setText(body);
        mailSender.send(message);
    }

    public void sendCartClearedMail(String toEmail, String username, List<String> productNames) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Your Cart Has Been Cleared -- WeLoveYou");

        StringBuilder body = new StringBuilder("Hi " + username + ",\n\n");
        body.append("You've removed all items from your cart. Here's what was cleared:\n");

        for (String productName : productNames) {
            body.append("- ").append(productName).append("\n");
        }

        body.append("\nWe hope to see you back soon for more customizations!\n\n");
        body.append("Warm regards,\n");
        body.append("The WeLoveYou Team");

        message.setText(body.toString());
        mailSender.send(message);
    }

    public void sendTransactionSuccessfulMail(String email, String username, byte[] receipt) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(email);
            helper.setSubject("Transaction Successful");

            String body = "Hi " + username + ",\n\n"
                    + "Your transaction was successful. Please find the transaction receipt attached.\n\n"
                    + "Warm regards,\n"
                    + "The WeLoveYou Team";

            helper.setText(body);
            // Attach PDF receipt
            helper.addAttachment("Transaction_Receipt.pdf", new ByteArrayResource(receipt));

            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendEmailWithAttachment(String to, byte[] pdfBytes, String filename) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject("Customization Image Preview");
        helper.setText("Hello, please find your customized product preview attached.");
        helper.addAttachment(filename, new ByteArrayResource(pdfBytes));
        mailSender.send(message);
    }

}
