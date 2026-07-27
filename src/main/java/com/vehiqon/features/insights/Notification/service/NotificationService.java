package com.vehiqon.features.insights.Notification.service;

import com.vehiqon.features.insights.Notification.dto.NotificationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    public boolean sendEmailAlert(String recipientEmail, String subject, String body) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(senderEmail);
            mailMessage.setTo(recipientEmail);
            mailMessage.setText(body);
            mailMessage.setSubject(subject);
            javaMailSender.send(mailMessage);
           log.info("Mail Sent Successfully!");
           return true;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }

    public void verificationEmail(NotificationDto.VerifyEmail request) {
         String body = "Welcome to Vehiqon!\n" +
                    "Please verify your email by clicking the link below.\n" +
                    "This link expires in 24 hours\n" + request.verificationUrl() + "\n" +
                    "If you did not create this account, you can safely ignore this email.";
         sendEmailAlert(request.email(), "Vehiqon -- Verify your email", body);
    }

    public void resetPassword(NotificationDto.ResetPassword request) {
        String body = "Click the link below to reset your password\n\n" +
                    "This link expires in 15 minutes\n\n" + request.resetPasswordUrl() + "\n" +
                    "If you did not reset your password, you can safely ignore this email.";
        sendEmailAlert(request.email(), "Vehiqon -- Reset Your Password", body);
    }

    public boolean maintenanceReminder(NotificationDto.MaintenanceReminder request) {
        String body = """
            Hello %s,

            This is a reminder that your vehicle is scheduled for maintenance.

            Service: %s

            Scheduled Date: %s

            Scheduled Time:%s

            Vehicle Brand: %s
            Vehicle Model: %s

            Please ensure your vehicle is available.

            Regards,
            Vehiqon
            """.formatted(
                    request.data().firstName() + " " + request.data().lastName(),
                    request.data().type(),
                    request.data().appointmentDate(),
                    request.data().appointmentTime(),
                    request.data().carBrandName(),
                    request.data().carModelName()
            );
        return sendEmailAlert(request.data().email(), "Vehicle Maintenance Reminder -" + request.data().type().name(), body);

    }


}