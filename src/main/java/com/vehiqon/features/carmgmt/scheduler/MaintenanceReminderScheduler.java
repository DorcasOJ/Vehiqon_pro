package com.vehiqon.features.carmgmt.scheduler;

import com.vehiqon.features.carmgmt.dto.CarMaintenanceDto;
import com.vehiqon.features.carmgmt.dto.response.MaintenanceReminderResponse;
import com.vehiqon.features.carmgmt.repository.CarMaintenanceRepository;
import com.vehiqon.features.email.mapper.EmailResponseMapper;
import com.vehiqon.features.email.service.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
        Optional<List<MaintenanceReminderResponse>> dueNotifications = carMaintenanceRepository.findDueNotifications(
                LocalDate.now()
        );

        if(dueNotifications.isPresent()) {
            List<MaintenanceReminderResponse> reminders = dueNotifications.get();
            for (MaintenanceReminderResponse reminder : reminders) {
                try {
                    emailService.sendEmailAlert(emailResponseMapper.maintenanceReminderEmail(reminder));
                    carMaintenanceRepository.markNotificationSent(reminder.id());
                } catch (Exception e) {
                    log.error("Failed to send reminder {}",
                            reminder.id(),
                            e);
                }
            }
            log.info("Attempted Sending {} maintenance reminders", reminders.size());
        }
    }
}
