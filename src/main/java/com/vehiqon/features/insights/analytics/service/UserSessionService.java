package com.vehiqon.features.insights.analytics.service;

import com.vehiqon.common.exception.BadRequestException;
import com.vehiqon.features.insights.InsightEventPublisher;
import com.vehiqon.features.insights.analytics.dto.AnalyticsDto;
import com.vehiqon.features.insights.analytics.entities.UserSessionEntity;
import com.vehiqon.features.insights.analytics.entities.aggregation.UserStatisticsEntity;
import com.vehiqon.features.insights.analytics.mapper.AnalyticsMapper;
import com.vehiqon.features.insights.analytics.repository.UserSessionRepository;
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
public class UserSessionService {
    private final InsightEventPublisher publisher;
    private final UserSessionRepository userSessionRepository;
    private final AnalyticsMapper analyticsMapper;
    private final StatisticsService statisticsService;
    @Value("${USER_SESSION_TIMEOUT}")
    private long USER_SESSION_TIMEOUT;



//    public void track(AnalyticsDto.AnalyticsEvent event) {
//        publisher.publish(event);
//    }

    public UserSessionEntity resolveUserSession(AnalyticsDto.SessionContext context) {
         LocalDateTime now = LocalDateTime.now();
        return
                userSessionRepository.findByUserIdAndDeviceIdAndLogoutAtIsNull(context
                        .userId(), context.deviceId())
                        .map(session -> {
                            if (session.getLastActivityAt()
                                    .isAfter(now.minusMinutes(USER_SESSION_TIMEOUT))) {
                                session.setLastActivityAt(now);
                                return userSessionRepository.save(session);
                            }
                            endSession(session,now);
                            return startUserSession(context, now);

                        }).orElseGet(()-> startUserSession(context, now));
    }

    public UserSessionEntity startUserSession(AnalyticsDto.SessionContext context, LocalDateTime startTime) {
        try {

            UserSessionEntity userSession = analyticsMapper.toStartUserSessionEntity(context);
            userSession.setId(context.userSessionId() == null ? UUID.randomUUID() : context.userSessionId());
            userSession.setLoginAt(startTime);
            userSession.setLastActivityAt(startTime);
            UserSessionEntity sessionEntity = userSessionRepository.save(userSession);
            statisticsService.incrementUserSession(sessionEntity.getUserId(), startTime);
            return sessionEntity;
        } catch (Exception e) {
            throw new BadRequestException("Failed to start user session: "+ e.getMessage());
        }

    }

    public UserSessionEntity updateUserSession(UserSessionEntity userSession) {
//        UserSessionEntity userSession= userSessionRepository.findById(userSessionId)
//                .orElseThrow(()-> new BadRequestException("user session does not exist, create one instead.")
//                );
        LocalDateTime now = LocalDateTime.now();
        userSession.setLastActivityAt(now);
        userSession.setDurationSeconds(
                Duration.between(userSession.getLoginAt(),  now).getSeconds()
        );
        UserSessionEntity sessionEntity = userSessionRepository.save(userSession);
        statisticsService.incrementUserEvent(sessionEntity.getUserId(), now);

        return sessionEntity;

    }

    public void endSession(UserSessionEntity userSession, LocalDateTime endTime) {
        userSession.setLogoutAt(endTime);
        userSession.setActive(false);
        userSession.setDurationSeconds(
                Duration.between(userSession.getLoginAt(), endTime).getSeconds());
        userSessionRepository.save(userSession);
        statisticsService.incrementUserTimeSpent(userSession.getUserId(),
                userSession.getDurationSeconds(), endTime);
    }

    public UserSessionEntity endSession(UUID userId, UUID deviceId, LocalDateTime endTime) {
        UserSessionEntity userSession = userSessionRepository.findByUserIdAndDeviceIdAndLogoutAtIsNull(userId, deviceId.toString())
                .orElseThrow(()-> new BadRequestException("user statistics does not exist, create one instead."));

        userSession.setLogoutAt(endTime);
        userSession.setActive(false);
        userSession.setDurationSeconds(
                Duration.between(userSession.getLoginAt(), endTime).getSeconds());
        UserSessionEntity sessionEntity = userSessionRepository.save(userSession);
        statisticsService.incrementUserTimeSpent(sessionEntity.getUserId(),
                sessionEntity.getDurationSeconds(), endTime);
        return sessionEntity;
    }

    public void endSessions(UserSessionEntity userSession) {
        endSession(userSession, LocalDateTime.now());
        userSessionRepository.logoutAllActiveSessions(userSession.getId(), LocalDateTime.now());
    }

    public void endSessionsWithDeviceId(UUID deviceId, UUID userId) {
        endSession(userId, deviceId, LocalDateTime.now());
        userSessionRepository.logoutAllActiveSessions(userId, LocalDateTime.now());
    }

    public void expireInactiveSession(LocalDateTime cutOffTime, LocalDateTime now) {
        int expiredCount = userSessionRepository.expireInactiveSessions(cutOffTime, now);
        if(expiredCount > 0) {
            log.info("Auto-ended {} inactive user session(s) older than {}", expiredCount, cutOffTime);
        }

    }


}
