package com.vehiqon.features.insights.analytics.scheduler;

import com.vehiqon.features.insights.analytics.repository.FeatureSessionRepository;
import com.vehiqon.features.insights.analytics.service.PersonalisationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonalisationServiceScheduler {
    private final PersonalisationService personalisationService;

//    Check Sessions (Runs every 30 minutes)
//    @Scheduled(cron = "0 0 2 * * ?")
//    @Scheduled(cron = "0 */50 * * * *")
    @Scheduled(cron = "0 45 * * * *")
    public void recalculateUserPersonalisationFavorites() {
        log.info("Starting scheduled calculation of user personalisation favorites...");
        long startTime = System.currentTimeMillis();
       try {
           personalisationService.refreshUserPersonalisationFavorites();
           long duration = System.currentTimeMillis() - startTime;
           log.info("Successfully updated user personalisation favorites in {} ms", duration);
       } catch (Exception e) {
           log.error("Failed to update user personalisation favorites", e);
       }
    }

}
