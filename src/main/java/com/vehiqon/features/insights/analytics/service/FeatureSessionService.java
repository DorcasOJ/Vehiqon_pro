package com.vehiqon.features.insights.analytics.service;

import com.vehiqon.features.insights.InsightEventPublisher;
import com.vehiqon.features.insights.analytics.entities.FeatureSessionEntity;
import com.vehiqon.features.insights.analytics.enums.EventType;
import com.vehiqon.features.insights.analytics.repository.FeatureSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeatureSessionService {
    private final InsightEventPublisher publisher;
    private final FeatureSessionRepository featureSessionRepository;
    private final StatisticsService statisticsService;
    @Value("${FEATURE_SESSION_TIMEOUT}")
    private long FEATURE_SESSION_TIMEOUT;

    
//    public void track(AnalyticsDto.AnalyticsEvent event) {
//        publisher.publish(event);
//    }


    public FeatureSessionEntity resolveFeatureSession(UUID userSessionId, UUID userId,
                                                        EventType type ) {
        LocalDateTime now = LocalDateTime.now();
        return featureSessionRepository.findByUserSessionIdAndFeatureAndEndedTimeIsNull(userSessionId,
                type.getFeature()).map(session -> {
                    if(session.getLastActivityTime()
                            .isAfter(now.minusMinutes(FEATURE_SESSION_TIMEOUT))) {
                        session.setLastActivityTime(now);
                        return featureSessionRepository.save(session);
                    }
                    endFeatureSession(session, session.getLastActivityTime());
                    return startFeatureSession(userId, userSessionId, type);
        }).orElseGet(() -> startFeatureSession(userId, userSessionId, type));

    }
    public FeatureSessionEntity startFeatureSession(UUID userId, UUID sessionId, EventType type) {
        LocalDateTime now = LocalDateTime.now();
        FeatureSessionEntity featureSession = FeatureSessionEntity.builder()
                .feature(type.getFeature())
                .startedTime(now)
                .lastActivityTime(now)
                .userSessionId(sessionId)
                .userId(userId)
                .build();
        FeatureSessionEntity entity = featureSessionRepository.save(featureSession);
        statisticsService.incrementFeatureVisit(entity.getUserId(),entity.getFeature() ,now);
        return entity;
    }

    public FeatureSessionEntity updateFeatureSession(FeatureSessionEntity featureSession) {
//        FeatureSessionEntity featureSession = featureSessionRepository.findByUserSessionIdAndFeatureAndEndedTimeIsNull(userSessionId, event.eventType().getFeature())
//                .orElseThrow(() -> new BadRequestException("Feature session does not exist, create new"));= featureSessionRepository.findByUserSessionIdAndFeatureAndEndedTimeIsNull(userSessionId, event.eventType().getFeature())
//                .orElseThrow(() -> new BadRequestException("Feature session does not exist, create new"));
        LocalDateTime now = LocalDateTime.now();
        featureSession.setLastActivityTime(now);
            featureSession.setDurationSeconds(
                    Duration.between(featureSession.getStartedTime(),now).getSeconds()
            );
        FeatureSessionEntity entity = featureSessionRepository.save(featureSession);
        statisticsService.incrementFeatureEvent(entity.getUserId(),entity.getFeature() ,now);
        return entity;
    }

    public FeatureSessionEntity switchFeature(FeatureSessionEntity oldFeatureSession, UUID userSessionId, EventType eventType) {

        LocalDateTime now = LocalDateTime.now();
        endFeatureSession(oldFeatureSession, now);
        return startFeatureSession(oldFeatureSession.getUserId(), userSessionId, eventType);
    }

    public void endFeatureSession(FeatureSessionEntity featureSession, LocalDateTime endedTime) {
//        FeatureSessionEntity featureSession = featureSessionRepository.findById(featureSessionId)
//                .orElseThrow(()-> new BadRequestException("feature session does not exist, create one"));
            featureSession.setEndedTime(endedTime);
            featureSession.setDurationSeconds(
                    Duration.between(featureSession.getStartedTime(), endedTime).getSeconds()
            );
        FeatureSessionEntity entity = featureSessionRepository.save(featureSession);
        statisticsService.incrementFeatureTime(entity.getUserId(),entity.getFeature() ,endedTime, entity.getDurationSeconds());

    }

    public void expireInactiveFeatureSession(LocalDateTime cutOffTime) {
        int expiredCount = featureSessionRepository.expireInactiveFeatureSessions(cutOffTime);
        if(expiredCount > 0) {
            log.info("Auto-ended {} inactive feature session(s) older than {}", expiredCount, cutOffTime);
        }
    }



}
