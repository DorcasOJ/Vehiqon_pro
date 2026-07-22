package com.vehiqon.features.insights.analytics.scheduler;

import com.vehiqon.features.insights.analytics.repository.FeatureSessionRepository;
import com.vehiqon.features.insights.analytics.repository.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionCleanupScheduler {
    private final UserSessionRepository userSessionRepository;
    private final FeatureSessionRepository featureSessionRepository;

//    Check User Sessions (Runs every 5 minutes)
    @Scheduled(cron = "0 */10 * * * *")
    public void cleanupInactiveSessions() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime cutOffTime = now.minusMinutes(30);


        int expiredCount = userSessionRepository.expireInactiveSessions(cutOffTime, now);
        if(expiredCount > 0) {
            log.info("Auto-ended {} inactive user session(s) older than {}", expiredCount, cutOffTime);
        }
    }

    @Scheduled(cron = "0 */10 * * * *")
    public void cleanupInactiveFeatures() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime cutOffTime = now.minusMinutes(30);
        int expiredCount = featureSessionRepository.expireInactiveFeatureSessions(cutOffTime, now);
        if(expiredCount > 0) {
            log.info("Auto-ended {} inactive feature session(s) older than {}", expiredCount, cutOffTime);
        }
    }


}
