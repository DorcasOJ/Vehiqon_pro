package com.vehiqon.features.insights.Notification.consumer;

import com.vehiqon.features.carmgmt.repository.MaintenanceReminderRepository;
import com.vehiqon.features.insights.Notification.dto.NotificationDto;
import com.vehiqon.features.insights.Notification.enums.NotificationEvent;
import com.vehiqon.features.insights.Notification.service.NotificationService;
import com.vehiqon.features.insights.enums.PublishAction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumer {
    public final NotificationService notificationService;
    public final MaintenanceReminderRepository maintenanceReminderRepository;

    private boolean shouldProcess(PublishAction action) {
        return action == PublishAction.NOTIFICATION;
    }

    @Async("asyncTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendEmailVerificationViaEmail(NotificationDto.VerifyEmail request) {
        if (!shouldProcess(request.publishAction())) {
            return;
        }
        if (request.notificationEvent() != NotificationEvent.VERIFY_EMAIL) {
            return;
        }
        try {
            notificationService.verificationEmail(request);
        } catch (Exception e) {
            log.error("Failed to send verification email. event = {}", request, e);
        }
    }

    @Async("asyncTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendResetPasswordViaEmail(NotificationDto.ResetPassword request) {
        if (!shouldProcess(request.publishAction())) {
            return;
        }
        try {
            notificationService.resetPassword(request);
        } catch (Exception e) {
            log.error("Failed to send reset password event {}", request, e);
        }
    }

    @Async("asyncTaskExecutor")
    @EventListener
    public void sendMaintenanceReminderViaEmail(NotificationDto.MaintenanceReminder request) {
        if (!shouldProcess(request.publishAction())) {
            return;
        }
        try {
            boolean sent = notificationService.maintenanceReminder(request);
            if(sent) {
                int updated = maintenanceReminderRepository.updateReminderNotificationStatusToSent(request.data().reminderId());
                if(updated == 0) {
                    log.warn(  "No maintenance reminder found to update. reminderId={}",
                            request.data().reminderId()
                    );
                }
                return;
            }
            maintenanceReminderRepository.updateReminderNotificationStatusToFailed(request.data().reminderId(),
                    "Email service returned false");
        } catch (Exception e) {
            maintenanceReminderRepository.updateReminderNotificationStatusToFailed(request.data().reminderId(),
                    e.getMessage());
            log.error("Failed to send maintenance reminder event {}", request, e);
        }
    }
}
