package com.vehiqon.features.carmgmt.scheduler;

import com.vehiqon.features.carmgmt.dto.response.MaintenanceReminderResponse;
import com.vehiqon.features.carmgmt.repository.MaintenanceReminderRepository;
import com.vehiqon.features.insights.InsightEventPublisher;
import com.vehiqon.features.insights.Notification.dto.NotificationDto;
import com.vehiqon.features.insights.Notification.enums.NotificationEvent;
import com.vehiqon.features.insights.enums.PublishAction;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class MaintenanceReminderScheduler {

    private final MaintenanceReminderRepository maintenanceReminderRepository;
    private final InsightEventPublisher publisher;

//    @Scheduled(cron = "0 * * * * *") // Every minute
    @Scheduled(cron = "0 0 8 * * *") // Every day at 8:00 AM
    @Transactional
    public void sendMaintenanceReminders() {

        Optional<List<MaintenanceReminderResponse>> dueReminderForScheduleOpt = maintenanceReminderRepository.findDueReminderForSchedule(
                Instant.now()
        );

        if(dueReminderForScheduleOpt.isPresent()) {
            List<MaintenanceReminderResponse> reminders = dueReminderForScheduleOpt.get();
            for (MaintenanceReminderResponse reminder : reminders) {
                try {
                    publisher.publish( new NotificationDto.MaintenanceReminder(
                            PublishAction.NOTIFICATION, reminder.maintenanceId(), reminder,
                            NotificationEvent.MAINTENANCE_REMINDER));

                    maintenanceReminderRepository.updateReminderNotificationStatusToQueued(reminder.reminderId());
                } catch (Exception e) {
                    log.error("Failed to send reminder {}", reminder.reminderId(), e);
                }
            }
            log.info("Attempted Sending {} maintenance reminders", reminders.size());
        }
    }
}
