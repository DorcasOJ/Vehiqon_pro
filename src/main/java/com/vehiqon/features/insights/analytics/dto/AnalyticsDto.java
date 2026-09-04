package com.vehiqon.features.insights.analytics.dto;

import com.vehiqon.common.api.dto.ConsumerEvent;
import com.vehiqon.features.insights.analytics.enums.EventType;
import com.vehiqon.features.insights.enums.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;


public class AnalyticsDto {

    private AnalyticsDto(){}

    public record AnalyticsEvent (
    UUID userId,
    EventType eventType,
    UUID entityId,
    Map<String, Object> metadata,
    LocalDateTime occurredAt,
    PublishAction publishAction,
    SessionContext sessionData
    ) implements ConsumerEvent {}

//    public record FeatureUsage(){}
//
//    public record Activity(){}
//
//    public record UsageTrend(){}
//
//    public record RecommendationSummary(){}


    public record SessionContext(
            UUID userId,
            UUID userSessionId,
            String ipAddress,
            String city,
            String country,
            String device,
            String browser,
            String platform,
            String operating_system,
            String appVersion,
            String deviceId,
            String deviceName
    ) {}
}
