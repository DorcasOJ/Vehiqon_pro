package com.vehiqon.features.insights.analytics.service;

import com.vehiqon.common.exception.BadRequestException;
import com.vehiqon.features.insights.analytics.dto.AnalyticsDto;
import com.vehiqon.features.insights.analytics.entities.FeatureSessionEntity;
import com.vehiqon.features.insights.analytics.entities.UserSessionEntity;
import com.vehiqon.features.insights.analytics.enums.EventType;
import com.vehiqon.features.insights.analytics.repository.*;
import com.vehiqon.features.insights.InsightEventPublisher;
import com.vehiqon.features.insights.enums.PublishAction;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticService {
    private final InsightEventPublisher publisher;
    private final UserSessionService userSessionService;
    private final FeatureSessionService featureSessionService;
    private final FeatureSessionRepository featureSessionRepository;
    private final StatisticsService statisticsService;
    private final AnalyticEventService analyticEventService;


    //    public void track(AnalyticsDto.AnalyticsEvent event) {
//        publisher.publish(event);
//    }
    @Async("asyncTaskExecutor")
    @Transactional
    public void sessionForLogin(AnalyticsDto.SessionContext context, Map<String, Object> metadata) {
        AnalyticsDto.AnalyticsEvent event = new AnalyticsDto.AnalyticsEvent(context.userId(), EventType.LOGIN,
                context.userId(), metadata, LocalDateTime.now(), PublishAction.ANALYTICS,context );
        UserSessionEntity userSessionEntity = userSessionService.resolveUserSession(context);
        FeatureSessionEntity featureSession = featureSessionService.resolveFeatureSession(userSessionEntity.getId(),
                userSessionEntity.getUserId(), EventType.LOGIN);
        analyticEventService.saveEvent(event, userSessionEntity.getId(), featureSession.getId());
//        statisticsService.updateLifetimeMetrics(userSessionEntity.getUserId(), EventType.LOGIN,
//                userSessionEntity.getDurationSeconds(), featureSession.getDurationSeconds());
    }

    @Async("asyncTaskExecutor")
    @Transactional
    public void endSessionForLogout(UserSessionEntity userSession) {

        FeatureSessionEntity featureSession = featureSessionRepository.findByUserSessionIdAndEndedTimeIsNull(userSession.getId())
                .orElseThrow(() -> new BadRequestException("No active feature, create new feature"));
        LocalDateTime now = LocalDateTime.now();
        featureSessionService.endFeatureSession(featureSession, now);
        userSessionService.endSession(userSession, now);
//        statisticsService.updateLifetimeMetrics(userSession.getUserId(), EventType.LOGOUT, userSession.getDurationSeconds(), featureSession.getDurationSeconds());
    }

    @Async("asyncTaskExecutor")
    @Transactional
    public void endSessionForLogout(UUID userId, UUID deviceId) {
        UserSessionEntity userSession = userSessionService.endSession(userId, deviceId, LocalDateTime.now());
        FeatureSessionEntity featureSession = featureSessionRepository.findByUserSessionIdAndEndedTimeIsNull(userSession.getId())
                .orElseThrow(() -> new BadRequestException("No active feature, create new feature"));
        featureSessionService.endFeatureSession(featureSession, LocalDateTime.now());
//        statisticsService.updateLifetimeMetrics(userSession.getUserId(), EventType.LOGOUT, userSession.getDurationSeconds(), featureSession.getDurationSeconds());
    }

    @Async("asyncTaskExecutor")
    public void endAllSessionWithDeviceId(UUID deviceId, UUID userId) {
        endSessionForLogout(userId, deviceId);
        userSessionService.endSessionsWithDeviceId(deviceId, userId);
    }


    @Async("asyncTaskExecutor")
    @Transactional
    public void endAllSession(UserSessionEntity userSession) {
        endSessionForLogout(userSession);
        userSessionService.endSessions(userSession);
    }

}
