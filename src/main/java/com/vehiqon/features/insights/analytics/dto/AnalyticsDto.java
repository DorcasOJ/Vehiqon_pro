package com.vehiqon.features.insights.analytics.dto;

import com.vehiqon.common.enums.EntityEnum;
import com.vehiqon.features.insights.analytics.enums.*;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;


public class AnalyticsDto {

    private AnalyticsDto(){}

    public interface event{
        UUID userId();
        UUID entityId();
        PublishAction publishAction();

    }

    public record AnalyticsEvent (
    UUID userId,
    FeatureEnum feature,
    EventType eventType,
    UUID userSessionId,
    UUID featureSessionId,
    EntityEnum entityType,
    UUID entityId,
    Map<String, Object> metadata,
    LocalDateTime occurredAt,
    PublishAction publishAction
    ) implements event{}

    public record AuditEvent(
            UUID userId,
            AuditAction action,
            EntityEnum entity,
            UUID entityId,
            AuditStatus status,
           HttpServletRequest request,
            PublishAction publishAction

    ) implements event{}

//    public record FeatureUsage(){}
//
//    public record Activity(){}
//
//    public record UsageTrend(){}
//
//    public record RecommendationSummary(){}

    public record SessionContext(
            String ipAddress,
            String city,
            String country,
            String device,
            String browser,
            String platform,
            String operating_system,
            String appVersion,
            String deviceId
    ) {}
}
