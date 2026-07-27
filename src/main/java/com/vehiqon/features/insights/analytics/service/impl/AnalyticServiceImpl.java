package com.vehiqon.features.insights.analytics.service.impl;

import com.vehiqon.common.exception.BadRequestException;
import com.vehiqon.features.insights.analytics.dto.AnalyticsDto;
import com.vehiqon.features.insights.analytics.entities.FeatureSessionEntity;
import com.vehiqon.features.insights.analytics.entities.UserSessionEntity;
import com.vehiqon.features.insights.analytics.entities.aggregation.UserFeatureStatisticsEntity;
import com.vehiqon.features.insights.analytics.entities.aggregation.UserPersonalisationEntity;
import com.vehiqon.features.insights.analytics.entities.aggregation.UserStatisticsEntity;
import com.vehiqon.features.insights.analytics.enums.EventType;
import com.vehiqon.features.insights.analytics.enums.FeatureEnum;
import com.vehiqon.features.insights.analytics.mapper.AnalyticsMapper;
import com.vehiqon.features.insights.analytics.repository.*;
import com.vehiqon.features.insights.InsightEventPublisher;
import com.vehiqon.features.insights.analytics.service.AnalyticsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticServiceImpl implements AnalyticsService {
    private final InsightEventPublisher publisher;
    private final FeatureSessionRepository featureSessionRepository;
    private final UserSessionRepository userSessionRepository;
    private final UserStatisticsRepository userStatisticsRepository;
    private final UserFeatureStatisticsRepository userFeatureStatisticsRepository;
    private final UserPersonalisationRepository userPersonalisationRepository;
    private final AnalyticsMapper analyticsMapper;

    @Override
    public void track(AnalyticsDto.AnalyticsEvent event) {
        publisher.publish(event);
    }

    @Override
    @Async("asyncTaskExecutor")
    public CompletableFuture<UUID> startFeatureSession(UUID userId, UUID sessionId, FeatureEnum feature, EventType type) {
        FeatureSessionEntity featureSession = FeatureSessionEntity.builder()
                .featureName(feature)
                .startedTime(LocalDateTime.now())
                .eventType(type)
                .userSessionId(sessionId)
                .userId(userId)
                .build();
        FeatureSessionEntity savedFeatureSession = featureSessionRepository.save(featureSession);
        Optional<UserFeatureStatisticsEntity> userFeatStatOpt = userFeatureStatisticsRepository.findByUserId(savedFeatureSession.getUserId());
        if(userFeatStatOpt.isPresent()) {
            UserFeatureStatisticsEntity userFeatureStatisticsEntity = userFeatStatOpt.get();
            userFeatureStatisticsEntity.setLastVisitedAt(LocalDateTime.now());
            userFeatureStatisticsEntity.setVisitCount(userFeatureStatisticsEntity.getVisitCount() + 1L);
            userFeatureStatisticsRepository.save(userFeatureStatisticsEntity);
        } else {
            UserFeatureStatisticsEntity userFeatureStatisticsEntity = UserFeatureStatisticsEntity.builder()
                    .userId(userId)
                    .feature(feature)
                    .lastVisitedAt(LocalDateTime.now())
                    .visitCount(1L)
                    .firstVisitedAt(LocalDateTime.now())
                    .build();
            userFeatureStatisticsRepository.save(userFeatureStatisticsEntity);
        }
        Optional<UserPersonalisationEntity> userPersonalisationOpt = userPersonalisationRepository.findByUserId(savedFeatureSession.getUserId());
        if (userPersonalisationOpt.isEmpty()) {
            UserPersonalisationEntity userPersonalisation = UserPersonalisationEntity.builder()
                    .userId(userId)
                    .build();
            userPersonalisationRepository.save(userPersonalisation);
        }
        return CompletableFuture.completedFuture(savedFeatureSession.getId());
    }

    @Override
    @Async("asyncTaskExecutor")
    public void endFeatureSession(UUID featureSessionId) {
        Optional<FeatureSessionEntity> featureSessionOpt = featureSessionRepository.findById(featureSessionId);
        if(featureSessionOpt.isPresent()) {
            FeatureSessionEntity featureSession = featureSessionOpt.get();
            featureSession.setEndedTime(LocalDateTime.now());
            featureSession.setDurationSeconds(
                    Duration.between(featureSession.getStartedTime(), featureSession.getEndedTime()).getSeconds()
            );
            featureSessionRepository.save(featureSession);

            Optional<UserFeatureStatisticsEntity> userFeatStatOpt = userFeatureStatisticsRepository.findByUserId(featureSession.getUserId());
            if(userFeatStatOpt.isPresent()) {
                UserFeatureStatisticsEntity userFeatureStatisticsEntity = userFeatStatOpt.get();
                userFeatureStatisticsEntity.setLastVisitedAt(LocalDateTime.now());
                userFeatureStatisticsEntity.setTotalDurationSeconds(userFeatureStatisticsEntity.getTotalDurationSeconds() + featureSession.getDurationSeconds());
                userFeatureStatisticsRepository.save(userFeatureStatisticsEntity);
            }
        }
    }

    @Override
    @Async("asyncTaskExecutor")
    @Transactional
    public void startUserSession(UUID id, UUID userId, AnalyticsDto.SessionContext context) {
        try {
            log.info("startUserSession called. sessionId={}, userId={}", id, userId);
            Optional<UserSessionEntity> userSessionOpt = userSessionRepository.findByUserIdAndDeviceIdAndLogoutAtIsNull(userId, context.deviceId());
            UserSessionEntity userSessionEntity;
            UserSessionEntity userSession;
            if (userSessionOpt.isPresent()) {
                userSession = userSessionOpt.get();
                userSession.setLastActivityAt(LocalDateTime.now());
            } else {
                userSession = analyticsMapper.toStartUserSessionEntity(context);
                userSession.setUserId(userId);
                userSession.setId(id);
                userSession.setLoginAt(LocalDateTime.now());
            }
            log.info("Before User Session save");
            userSessionEntity = userSessionRepository.save(userSession);
            log.info("After User Session save. id={}", userSessionEntity.getId());
            Optional<UserStatisticsEntity> userStatisticsEntityOpt = userStatisticsRepository.findByUserId(userId);
            if (userStatisticsEntityOpt.isPresent()) {
                UserStatisticsEntity userStatisticsEntity = userStatisticsEntityOpt.get();
                userStatisticsEntity.setTotalSessions(userStatisticsEntity.getTotalSessions() + 1L);
                userStatisticsEntity.setLastActive(LocalDateTime.now());
                userStatisticsRepository.save(userStatisticsEntity);
            } else {
                UserStatisticsEntity userStatistics = UserStatisticsEntity.builder()
                        .totalSessions(1L)
                        .lastActive(LocalDateTime.now())
                        .userId(userId)
                        .build();
                userStatisticsRepository.save(userStatistics);
            }
            return;
//            return CompletableFuture.completedFuture(userSessionEntity.getId());
        } catch (Exception e) {
            throw new BadRequestException("Failed to start user session: "+ e.getMessage());
        }
    }

    @Override
    @Async("asyncTaskExecutor")
    public void endSession(UUID userSessionId) {
        Optional<UserSessionEntity> userSessionOpt = userSessionRepository.findById(userSessionId);
        if(userSessionOpt.isPresent()) {
            UserSessionEntity userSession = userSessionOpt.get();
            userSession.setLogoutAt(LocalDateTime.now());
            userSession.setActive(false);
            userSession.setDurationSeconds(
                    Duration.between(userSession.getLoginAt(), userSession.getLogoutAt()).getSeconds());
            userSessionRepository.save(userSession);
// end feature session and featureStatistics before endSession...
            Optional<UserStatisticsEntity> userStatisticsEntityOpt = userStatisticsRepository.findByUserId(userSession.getUserId());
            if (userStatisticsEntityOpt.isPresent()) {
                UserStatisticsEntity userStatisticsEntity = userStatisticsEntityOpt.get();
                userStatisticsEntity.setTotalTimeSpent(
                        userStatisticsEntity.getTotalTimeSpent() + userSession.getDurationSeconds()
                );
                userStatisticsEntity.setLastActive(LocalDateTime.now());
                userStatisticsRepository.save(userStatisticsEntity);
            }
        }
    }

    @Override
    @Async("asyncTaskExecutor")
    public void endAllSession(UUID userSessionId, UUID userId) {
        endSession(userSessionId);
        userSessionRepository.logoutAllActiveSessions(userId, LocalDateTime.now());
    }
}
