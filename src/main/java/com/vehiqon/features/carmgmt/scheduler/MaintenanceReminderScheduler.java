package com.vehiqon.features.carmgmt.scheduler;

import com.vehiqon.features.carmgmt.entities.MaintenanceReminderEntity;
import com.vehiqon.features.carmgmt.repository.CarMaintenanceRepository;
import com.vehiqon.features.email.dto.EmailDetails;
import com.vehiqon.features.email.mapper.EmailResponseMapper;
import com.vehiqon.features.email.service.EmailService;
import com.vehiqon.features.onboarding.entity.UserEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MaintenanceReminderScheduler {

    private final CarMaintenanceRepository carMaintenanceRepository;
    private final EmailService emailService;
    private final EmailResponseMapper emailResponseMapper;

//    @Scheduled(cron = "0 * * * * *") // Every minute
    @Scheduled(cron = "0 0 8 * * *") // Every day at 8:00 AM
    @Transactional
    public void sendMaintenanceReminders() {
        List<MaintenanceReminderEntity> reminders = carMaintenanceRepository.findAllByNotificationSentFalseAndNotificationDateLessThanEqual(
                LocalDate.now()
        );
        for (MaintenanceReminderEntity reminderEntity : reminders) {
            try {
                UserEntity user = reminderEntity.getCarEntity().getUser();

                emailService.sendEmailAlert(emailResponseMapper.maintenanceReminderEmail(user,reminderEntity));
                reminderEntity.setNotificationSent(true);
            } catch (Exception e) {
                log.error("Failed to send reminder {}",
                        reminderEntity.getId(),
                        e);
            }
        }
        carMaintenanceRepository.saveAll(reminders);
        log.info("Attempted Sending {} maintenance reminders", reminders.size());
    }
}
