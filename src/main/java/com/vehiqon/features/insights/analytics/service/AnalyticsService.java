package com.vehiqon.features.insights.analytics.service;

import com.vehiqon.features.insights.analytics.dto.AnalyticsDto;
import com.vehiqon.features.insights.analytics.enums.EventType;
import com.vehiqon.features.insights.analytics.enums.FeatureEnum;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface AnalyticsService {
    void track(AnalyticsDto.AnalyticsEvent event);
    CompletableFuture<UUID> startFeatureSession(UUID userId, UUID sessionId, FeatureEnum feature, EventType eventType);
    void endFeatureSession(UUID featureSessionId);
    CompletableFuture<UUID> startUserSession(UUID id, UUID userId, AnalyticsDto.SessionContext context);
    void endSession(UUID userSessionId);
    void endAllSession(UUID userSessionId, UUID userId);
}
