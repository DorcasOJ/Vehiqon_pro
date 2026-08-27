package com.vehiqon.features.insights.analytics.service.consumer;

import com.vehiqon.common.exception.BadRequestException;
import com.vehiqon.features.insights.analytics.dto.AnalyticsDto;
import com.vehiqon.features.insights.analytics.entities.FeatureSessionEntity;
import com.vehiqon.features.insights.analytics.entities.UserSessionEntity;
import com.vehiqon.features.insights.analytics.repository.FeatureSessionRepository;
import com.vehiqon.features.insights.analytics.service.*;
import com.vehiqon.features.insights.enums.PublishAction;
import com.vehiqon.features.onboarding.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class AnalyticsConsumer {
    private final FeatureSessionRepository featureSessionRepository;
    private final AnalyticEventService analyticEventService;
    private final FeatureSessionService featureSessionService;
    private final PersonalisationService personalisationService;
    private final StatisticsService statisticsService;
    private final UserSessionService userSessionService;
    private final AuthService authService;

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
//    @EventListener
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT,
            fallbackExecution = true)
    @Transactional
    public void consume(AnalyticsDto.AnalyticsEvent event) {

        if (event.publishAction() != PublishAction.ANALYTICS) {
            return;
        }
        if(event.userId() == null) {
            throw new BadRequestException("User Id is required for analytics event to consume");
        }
        try {
//            FEATURE = event.feature() -> find active feature
//            -> same feature? update (last activity) else close previous
//            create new feature -> save analytics -> update all statistics\
//            publish personalisation event
            runAnalytics(event);
        } catch (Exception e) {
            log.error("Failed to process analytics event {}", event, e);
        }
    }

    private void runAnalytics(AnalyticsDto.AnalyticsEvent event) {
        UserSessionEntity userSession = userSessionService.resolveUserSession(event.sessionData());
        System.out.println("Resolved user session");
        userSessionService.updateUserSession(userSession);
        System.out.println("Updated user session");

        FeatureSessionEntity featureSessionEntity = featureSessionService.resolveFeatureSession(userSession.getId(), userSession.getUserId(), event.eventType());
        System.out.println("Resolved feature session");

        if(Objects.equals(event.eventType().getFeature(), featureSessionEntity.getFeature())) {
         featureSessionEntity = featureSessionService.updateFeatureSession(featureSessionEntity);

        } else {
            featureSessionEntity = featureSessionService.switchFeature(featureSessionEntity, event.sessionData().userSessionId(), event.eventType());
        }
//        System.out.println("Updated feature session");

        analyticEventService.saveEvent(event, userSession.getId(), featureSessionEntity.getId());
//        System.out.println("saved event  session");

    }



}