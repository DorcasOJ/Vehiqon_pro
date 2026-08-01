package com.vehiqon.features.insights.analytics.scheduler;

import com.vehiqon.features.insights.analytics.repository.FeatureSessionRepository;
import com.vehiqon.features.insights.analytics.service.FeatureSessionService;
import com.vehiqon.features.insights.analytics.service.UserSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionCleanupScheduler {
    private final UserSessionService userSessionService;
    private final FeatureSessionService featureSessionService;
    private final FeatureSessionRepository featureSessionRepository;

//    Check Sessions (Runs every 30 minutes)
    @Scheduled(cron = "0 */30 * * * *")
    public void cleanupInactiveSessions() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime cutOffTime = now.minusMinutes(30);
        userSessionService.expireInactiveSession(cutOffTime, now);
    }

    @Scheduled(cron = "0 */30 * * * *")
    public void cleanupInactiveFeatures() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime cutOffTime = now.minusMinutes(30);
        featureSessionService.expireInactiveFeatureSession(cutOffTime);
    }



}
