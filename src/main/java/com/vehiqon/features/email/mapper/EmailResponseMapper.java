package com.vehiqon.features.email.mapper;


import com.vehiqon.features.carmgmt.dto.CarMaintenanceDto;
import com.vehiqon.features.carmgmt.dto.response.MaintenanceReminderResponse;
import com.vehiqon.features.email.dto.EmailDetails;
import com.vehiqon.features.onboarding.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class EmailResponseMapper {

    public EmailDetails toVerifyEmailResponse(UserEntity savedUser, String url) {

        return EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("Vehiqon -- Verify your email")
                .messageBody("Welcome to Vehiqon!\n" +
                        "Please verify your email by clicking the link below.\n" +
                        "This link expires in 24 hours\n" + url + "\n" +
                        "If you did not create this account, you can safely ignore this email."
                )
                .build();
    }

    public EmailDetails maintenanceReminderEmail(MaintenanceReminderResponse reminderEntity) {

        String body = """
            Hello %s,

            This is a reminder that your vehicle is scheduled for maintenance.

            Service:
            %s

            Scheduled Date:
            %s
            
            Scheduled Time:
            %s

            Vehicle:
            %s %s

            Please ensure your vehicle is available.

            Regards,
            Vehiqon
            """
                .formatted(
                        reminderEntity.firstName(),
                        reminderEntity.type(),
                        reminderEntity.appointmentDate().toString(),
                        reminderEntity.appointmentTime().toString(),
                        reminderEntity.carBrandName(),
                        reminderEntity.carModelName()
                );

        return EmailDetails.builder()
                .recipient(reminderEntity.email())
                .subject("Vehicle Maintenance Reminder -" + reminderEntity.type().name())
                .messageBody(body)
                .build();
    }

//    public EmailDetails toAccountCreationResponse(UserEntity savedUser) {
//
//           return EmailDetails.builder()
//                   .recipient(savedUser.getEmail())
//                   .subject("Account Creation")
//                   .messageBody("Congratulations your Account have been successfully created.\n" +
//                           "Your Primary Account Details:\n" +
//                           "Account Name: " + savedUser.getFirstName() + " " + savedUser.getLastName() + "\n" +
//                           "Account NUBAN:" + savedUser.getPrimaryAccountNumber())
//                   .build();
//
//        }
    }
