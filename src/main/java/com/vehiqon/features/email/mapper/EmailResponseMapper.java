package com.vehiqon.features.email.mapper;


import com.vehiqon.features.carmgmt.entities.MaintenanceReminderEntity;
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

    public EmailDetails maintenanceReminderEmail(UserEntity user, MaintenanceReminderEntity reminderEntity) {

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
                        user.getFirstName(),
                        reminderEntity.getType(),
                        reminderEntity.getAppointmentDate().toString(),
                        reminderEntity.getAppointmentTime().toString(),
                        reminderEntity.getCarEntity().getBrand().getName(),
                        reminderEntity.getCarEntity().getModel().getName()
                );

//         Your vehicle is due for" + reminderEntity.getType().name() +
//                " on " + reminderEntity.getDueDate().toString() +
//                " at " + reminderEntity.getWorkshop() +
//                ".\n Kindly reach out to us on " + reminderEntity.getAppointmentDate() +
//                " by " + reminderEntity.getAppointmentDate().toString())

        return EmailDetails.builder()
                .recipient(user.getEmail())
                .subject("Vehicle Maintenance Reminder -" + reminderEntity.getType().name())
                .messageBody(body)
                .build();
    }

    public EmailDetails toAccountCreationResponse(UserEntity savedUser) {

           return EmailDetails.builder()
                   .recipient(savedUser.getEmail())
                   .subject("Account Creation")
                   .messageBody("Congratulations your Account have been successfully created.\n" +
                           "Your Primary Account Details:\n" +
                           "Account Name: " + savedUser.getFirstName() + " " + savedUser.getLastName() + "\n" +
                           "Account NUBAN:" + savedUser.getPrimaryAccountNumber())
                   .build();

        }
    }
