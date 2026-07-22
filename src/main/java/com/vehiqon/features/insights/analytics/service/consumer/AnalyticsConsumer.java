package com.vehiqon.features.insights.analytics.service.consumer;

import com.vehiqon.common.exception.BadRequestException;
import com.vehiqon.features.insights.analytics.dto.AnalyticsDto;
import com.vehiqon.features.insights.analytics.entities.FeatureSessionEntity;
import com.vehiqon.features.insights.analytics.entities.UserSessionEntity;
import com.vehiqon.features.insights.analytics.entities.aggregation.UserFeatureStatisticsEntity;
import com.vehiqon.features.insights.analytics.entities.aggregation.UserStatisticsEntity;
import com.vehiqon.features.insights.analytics.enums.PublishAction;
import com.vehiqon.features.insights.analytics.mapper.AnalyticsMapper;
import com.vehiqon.features.insights.analytics.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class AnalyticsConsumer {
    private final UserEventRepository userEventRepository;
    private final UserSessionRepository userSessionRepository;
    private final FeatureSessionRepository featureSessionRepository;

    private final UserStatisticsRepository userStatisticsRepository;
    private final UserFeatureStatisticsRepository userFeatureStatisticsRepository;
    private final UserPersonalisationRepository userPersonalisationRepository;
    private final AnalyticsMapper userEventMapper;

//      ├── Save user_events
//        ├── Update user_sessions
//        ├── Update feature_sessions
//        ├── Increment user_statistics
//        ├── Update user_feature_statistics
//        ├── Refresh user_personalisation
//        └── Publish InsightsUpdatedEvent (optional)


//    on AnalyticsEvent
//    ├── eventRecorder.record(...)
//            ├── sessionTracker.process(...)
//            ├── statisticsAggregator.aggregate(...)
//            ├── personalizationEngine.update(...)
//            └── recommendationEngine.refresh(...)

    @Async("asyncTaskExecutor")
    @EventListener
    public void consume(AnalyticsDto.AnalyticsEvent event) {

        if (event.publishAction() != PublishAction.ANALYTICS) {
            return;
        }
        if(event.userId() == null) {
            throw new BadRequestException("User Id is required");
        }
        try {
            System.out.println(event.eventType());
            recordSessions(event);
            updateUserSession(event);
            updateFeatureSession(event);
            updateUserStatistics(event);
            updateFeatureStatistics(event);
        } catch (Exception e) {
            log.error("Failed to process analytics event {}", event, e);
//            throw new BadRequestException(e.getMessage());
        }

    }

    public void recordSessions(AnalyticsDto.AnalyticsEvent event) {
        userEventRepository.save(userEventMapper.toUserEntityResponse(event));
    }

    public void updateUserSession(AnalyticsDto.AnalyticsEvent event) {
        Optional<UserSessionEntity> userSessionEntityOptional = userSessionRepository.findById(event.userSessionId());
        if(userSessionEntityOptional.isPresent()) {
            UserSessionEntity userSession = userSessionEntityOptional.get();
            userSession.setLastActivityAt(LocalDateTime.now());
            userSession.setDurationSeconds(
                    Duration.between(userSession.getLastActivityAt(),  userSession.getLoginAt()).getSeconds()
            );
            userSessionRepository.save(userSession);
        }

    }

    public void updateFeatureSession(AnalyticsDto.AnalyticsEvent event) {
        Optional<FeatureSessionEntity> featureSessionEntityOptional = featureSessionRepository.findByUserSessionIdAndFeatureNameAndEndedTimeIsNull(event.userSessionId(), event.feature());
        if(featureSessionEntityOptional.isPresent()) {
            FeatureSessionEntity featureSession = featureSessionEntityOptional.get();
            featureSession.setLastActivityTime(LocalDateTime.now());
            featureSession.setDurationSeconds(
                    Duration.between(featureSession.getLastActivityTime(),  featureSession.getStartedTime()).getSeconds()
            );
            featureSessionRepository.save(featureSession);
        }

    }

    public void updateUserStatistics(AnalyticsDto.AnalyticsEvent event) {
        Optional<UserStatisticsEntity> userStatisticsEntityOpt = userStatisticsRepository.findByUserId(event.userId());
        if (userStatisticsEntityOpt.isPresent()) {
            UserStatisticsEntity userStatisticsEntity = userStatisticsEntityOpt.get();
            userStatisticsEntity.setLastActive(LocalDateTime.now());
            userStatisticsEntity.setTotalEvents(
                    userStatisticsEntity.getTotalEvents() + 1L
            );
            userStatisticsRepository.save(userStatisticsEntity);
        }
    }

    public void updateFeatureStatistics(AnalyticsDto.AnalyticsEvent event) {
        Optional<UserFeatureStatisticsEntity> featureStatisticsEntityOpt = userFeatureStatisticsRepository.findByUserId(event.userId());
        if (featureStatisticsEntityOpt.isPresent()) {
            UserFeatureStatisticsEntity featureStatisticsEntity = featureStatisticsEntityOpt.get();
            featureStatisticsEntity.setLastVisitedAt(LocalDateTime.now());
            featureStatisticsEntity.setTotalEvents(
                    featureStatisticsEntity.getTotalEvents() + 1L
            );
            userFeatureStatisticsRepository.save(featureStatisticsEntity);
        }
    }
}