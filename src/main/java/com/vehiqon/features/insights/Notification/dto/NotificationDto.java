package com.vehiqon.features.insights.Notification.dto;

import com.vehiqon.common.dto.ConsumerEvent;
import com.vehiqon.features.carmgmt.dto.response.MaintenanceReminderResponse;
import com.vehiqon.features.insights.Notification.enums.NotificationEvent;
import com.vehiqon.features.insights.enums.PublishAction;

import java.util.UUID;

public class NotificationDto {
    private NotificationDto(){}

    public record VerifyEmail(
            PublishAction publishAction,
            UUID entityId,
           String email,
            String verificationUrl,
            NotificationEvent notificationEvent
    ) implements ConsumerEvent {}

    public record ResetPassword(
            PublishAction publishAction,
            UUID entityId,
            String email,
            String resetPasswordUrl,
            NotificationEvent notificationEvent
    ) implements ConsumerEvent {}

    public record MaintenanceReminder(
            PublishAction publishAction,
            UUID entityId,
            MaintenanceReminderResponse data,
            NotificationEvent notificationEvent

    ) implements ConsumerEvent {

    }




}