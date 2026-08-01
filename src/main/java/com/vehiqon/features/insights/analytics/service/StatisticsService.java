package com.vehiqon.features.insights.analytics.service;

import com.vehiqon.features.insights.InsightEventPublisher;
import com.vehiqon.features.insights.analytics.entities.aggregation.UserFeatureStatisticsEntity;
import com.vehiqon.features.insights.analytics.entities.aggregation.UserStatisticsEntity;
import com.vehiqon.features.insights.analytics.enums.EventType;
import com.vehiqon.features.insights.analytics.enums.FeatureEnum;
import com.vehiqon.features.insights.analytics.repository.UserFeatureStatisticsRepository;
import com.vehiqon.features.insights.analytics.repository.UserStatisticsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticsService {
    private final InsightEventPublisher publisher;
    private final UserStatisticsRepository userStatisticsRepository;
    private final UserFeatureStatisticsRepository userFeatureStatisticsRepository;

    public UserFeatureStatisticsEntity resolveUserFeatureStatisticsEntity(UUID userId, FeatureEnum feature, LocalDateTime now) {
        return userFeatureStatisticsRepository.findByUserIdAndFeature(userId, feature)
        .orElseGet(()->{
                    UserFeatureStatisticsEntity entity = new UserFeatureStatisticsEntity();
                    entity.setUserId(userId);
                    entity.setFeature(feature);
                    entity.setFirstVisitedAt(now);
                    entity.setVisitCount(0L);
                    entity.setTotalEvents(0L);
                    entity.setTotalDurationSeconds(0L);
                    return entity;
                });
    }

    public UserStatisticsEntity resolveUserStatisticsEntity(UUID userId) {
        return userStatisticsRepository.findByUserId(userId)
                        .orElseGet(()-> {
                            UserStatisticsEntity entity = new UserStatisticsEntity();
                             entity.setTotalSessions(0L);
                             entity.setTotalTimeSpent(0L);
                             entity.setTotalEvents(0L);
                            entity.setLastActive(LocalDateTime.now());
                            entity.setUserId(userId);
                            return entity;
                        });
    }

    public void incrementUserSession(UUID userId, LocalDateTime lastActive) {
        UserStatisticsEntity statisticsEntity = resolveUserStatisticsEntity(userId);
        statisticsEntity.setTotalSessions(statisticsEntity.getTotalSessions() +1L);
        statisticsEntity.setLastActive(lastActive);
        userStatisticsRepository.save(statisticsEntity);
    }

    public void incrementUserEvent(UUID userId, LocalDateTime lastActive) {
        UserStatisticsEntity statisticsEntity = resolveUserStatisticsEntity(userId);
        statisticsEntity.setTotalEvents(statisticsEntity.getTotalEvents() +1L);
        statisticsEntity.setLastActive(lastActive);
        userStatisticsRepository.save(statisticsEntity);
    }

    public void incrementUserTimeSpent(UUID userId, Long userSessionDurationSeconds, LocalDateTime lastActive) {
        UserStatisticsEntity statisticsEntity = resolveUserStatisticsEntity(userId);
        statisticsEntity.setTotalTimeSpent(
                statisticsEntity.getTotalTimeSpent() + userSessionDurationSeconds
        );
        statisticsEntity.setLastActive(lastActive);
        userStatisticsRepository.save(statisticsEntity);
    }

    public void incrementFeatureVisit(UUID userId, FeatureEnum feature, LocalDateTime now) {
        UserFeatureStatisticsEntity featureStatistics = resolveUserFeatureStatisticsEntity(userId, feature, now);
        featureStatistics.setLastVisitedAt(now);
        featureStatistics.setVisitCount(featureStatistics.getVisitCount() + 1L);
        userFeatureStatisticsRepository.save(featureStatistics);
    }

    public void incrementFeatureEvent(UUID userId, FeatureEnum feature, LocalDateTime now) {
        UserFeatureStatisticsEntity featureStatistics = resolveUserFeatureStatisticsEntity(userId, feature, now);
        featureStatistics.setTotalEvents(featureStatistics.getTotalEvents() + 1L);
        featureStatistics.setLastVisitedAt(now);
        userFeatureStatisticsRepository.save(featureStatistics);
    }

    public void incrementFeatureTime(UUID userId, FeatureEnum feature, LocalDateTime now, long featureDurationSeconds) {
        UserFeatureStatisticsEntity featureStatistics = resolveUserFeatureStatisticsEntity(userId, feature, now);
        featureStatistics.setTotalDurationSeconds(featureStatistics.getTotalDurationSeconds() + featureDurationSeconds);
        featureStatistics.setLastVisitedAt(now);
        userFeatureStatisticsRepository.save(featureStatistics);
    }



//    public void updateFeatureStatistics(UUID userId, EventType eventType, Long featureDurationSeconds) {
//        LocalDateTime now = LocalDateTime.now();
//        UserFeatureStatisticsEntity featureStatisticsEntity = resolveUserFeatureStatisticsEntity(userId, eventType, now);
//
//        featureStatisticsEntity.setLastVisitedAt(now);
//        featureStatisticsEntity.setTotalDurationSeconds(featureStatisticsEntity.getTotalDurationSeconds() + featureDurationSeconds);
//        featureStatisticsEntity.setTotalEvents(
//                featureStatisticsEntity.getTotalEvents() + 1L
//        );
//        featureStatisticsEntity.setVisitCount(featureStatisticsEntity.getVisitCount() + 1L);
//        userFeatureStatisticsRepository.save(featureStatisticsEntity);
//
//    }


    //    in scheduled jobs
    public void calculateScores() {
//        activityScore =
//                loginFrequency
//                        + eventsToday
//                        + activeDays
//        engagementScore =
//                featureUsage
//                        + averageSessionLength
//                        + completedMaintenance

//        vehicleHealthScore =
//                completedMaintenance
//                        - overdueMaintenance
    }


//    public void updateUserStatistics(UUID userId, Long userSessionDurationSeconds) {
//        UserStatisticsEntity userStatisticsEntity = resolveUserStatisticsEntity(userId);
//        userStatisticsEntity.setLastActive(LocalDateTime.now());
//        userStatisticsEntity.setTotalSessions(userStatisticsEntity.getTotalSessions() + 1L);
//        userStatisticsEntity.setTotalEvents(
//                userStatisticsEntity.getTotalEvents() + 1L
//        );
//        userStatisticsEntity.setTotalTimeSpent(
//                userStatisticsEntity.getTotalTimeSpent() + userSessionDurationSeconds
//        );
//        userStatisticsRepository.save(userStatisticsEntity);
//    }


//    public void updateLifetimeMetrics(UUID userId, EventType eventType, Long userSessionDurationSeconds, Long featureDurationSeconds) {
//        updateUserStatistics(userId, userSessionDurationSeconds);
//        updateFeatureStatistics(userId, eventType, featureDurationSeconds);

//        statistics.totalSessions++;
//        statistics.totalEvents++;
//        statistics.totalTimeSpent += sessionDuration;
//        statistics.lifetimeSpending += paymentAmount;
//    }

//    private void endUserStatistics(UUID userId, Long userSessionDurationSeconds) {
//        UserStatisticsEntity userStatisticsEntity = userStatisticsRepository.findByUserId(userId)
//                .orElseThrow(()-> new BadRequestException("user statistics does not exist, create one instead."));
//        userStatisticsEntity.setTotalTimeSpent(
//                userStatisticsEntity.getTotalTimeSpent() + userSessionDurationSeconds
//        );
//        userStatisticsEntity.setLastActive(LocalDateTime.now());
//        userStatisticsRepository.save(userStatisticsEntity);
//    }


}
