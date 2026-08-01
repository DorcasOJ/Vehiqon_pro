package com.vehiqon.features.insights.analytics.service;

import com.vehiqon.common.exception.BadRequestException;
import com.vehiqon.features.insights.InsightEventPublisher;
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
public class PersonalisationService {
    private final InsightEventPublisher publisher;
    private final FeatureSessionRepository featureSessionRepository;
    private final UserSessionRepository userSessionRepository;
    private final UserStatisticsRepository userStatisticsRepository;
    private final UserFeatureStatisticsRepository userFeatureStatisticsRepository;
    private final UserPersonalisationRepository userPersonalisationRepository;
    private final AnalyticsMapper analyticsMapper;

    
//    public void track(AnalyticsDto.AnalyticsEvent event) {
//        publisher.publish(event);
//    }


    private void updateUserPersonalisation(UUID userId, UUID featureSessionId) {
        Optional<UserPersonalisationEntity> userPersonalisationOpt = userPersonalisationRepository.findByUserId(featureSessionId);
        if (userPersonalisationOpt.isEmpty()) {
            UserPersonalisationEntity userPersonalisation = UserPersonalisationEntity.builder()
                    .userId(userId)
                    .build();
            userPersonalisationRepository.save(userPersonalisation);
        }
    }

    public void updatePreference() {
//        Every event should affect user preferences.
//        count the number of times a feature is opened or used in user_event table
//        calculate the percentage, use that to det the favourite feature
//        recommend the upcoming/update of that feature to user on dashboard
//        updateFeaturePreference();
//        updateVehiclePreference();
//        updatePaymentPreference();
    }
    public void learnBehaviour() {
//        how does the user behave
//        in terms sof pattern, what feature/ event does user visit, an in what order
//        detectBehaviourPattern();
    }


    public void refreshUserPersonalisationFavorites() {
        userPersonalisationRepository.refreshUserPersonalisationFavourites();
    }
}
