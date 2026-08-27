package com.vehiqon.features.carmgmt.repository;

import com.vehiqon.features.carmgmt.dto.response.MaintenanceReminderResponse;
import com.vehiqon.features.insights.Notification.dto.NotificationDto;
import com.vehiqon.features.carmgmt.entities.MaintenanceReminderEntity;
import com.vehiqon.features.carmgmt.enums.NotificationStatus;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MaintenanceReminderRepository extends JpaRepository<MaintenanceReminderEntity, UUID> {

    Optional<List<MaintenanceReminderEntity>> findByCarMaintenanceId(UUID maintenanceId);
    Optional<List<MaintenanceReminderEntity>> findByNotificationStatus(NotificationStatus notificationStatus);
    Optional<List<MaintenanceReminderEntity>> findByScheduledAtBeforeAndNotificationStatus(Instant now, NotificationStatus notificationStatus);
    Optional<List<MaintenanceReminderEntity>> findByScheduledAtBetween(Instant start, Instant end);
    Optional<MaintenanceReminderEntity> findFirstByCarMaintenanceIdOrderByScheduledAtAsc(UUID maintenanceId);
    Optional<MaintenanceReminderEntity> findFirstByCarMaintenanceIdOrderByScheduledAtDesc(UUID maintenanceId);

    long countByNotificationStatus(UUID reminderId);
    Optional<List<MaintenanceReminderEntity>> findByNotificationStatusOrderByScheduledAtAsc(NotificationStatus notificationStatus);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT r FROM MaintenanceReminderEntity r
            WHERE r.id = :id
            """)
    Optional<MaintenanceReminderEntity> lockById(@Param("id") UUID id);

    @Query("""
            SELECT r FROM MaintenanceReminderEntity r
            WHERE r.notificationStatus = :documentStatus
            AND r.scheduledAt <= :now
            ORDER BY r.scheduledAt
            """)
    Optional<List<MaintenanceReminderEntity>> findDueReminders(@Param("documentStatus") NotificationStatus notificationStatus,
                                                               @Param("now") Instant now);

    @Query("""
    SELECT new com.vehiqon.features.carmgmt.dto.response.MaintenanceReminderResponse(
        mr.id,
        u.email,
        u.firstName,
        u.lastName,
        u.phoneNumber,
    
        m.appointmentDate,
        m.appointmentTime,
        m.estimatedCost,
    
        m.id,
        m.title,
        m.workshop,
        
        c.id,
        cb.name,
        cm.name,
        c.nickname,
        m.maintenanceType
    )
    FROM MaintenanceReminderEntity mr
    JOIN CarMaintenanceEntity m ON mr.carMaintenanceId = m.id
    JOIN CarEntity c ON m.carId = c.id
    JOIN BrandEntity cb ON c.carBrandId = cb.id
    JOIN CarModelEntity cm ON c.carModelId = cm.id
    JOIN UserEntity u ON c.userId = u.id

    WHERE mr.notificationStatus = com.vehiqon.features.carmgmt.enums.NotificationStatus.PENDING
    AND mr.scheduledAt <= :now
    ORDER BY mr.scheduledAt ASC
    """)
    Optional<List<MaintenanceReminderResponse>> findDueReminderForSchedule(@Param("now") Instant now);

    @Modifying
    @Transactional
    @Query("""
            UPDATE MaintenanceReminderEntity m
            SET m.notificationStatus=com.vehiqon.features.carmgmt.enums.NotificationStatus.QUEUED,
            m.queuedAt = CURRENT_TIMESTAMP
            WHERE m.id = :id
            """)
    void updateReminderNotificationStatusToQueued(@Param("id") UUID reminderId);

    @Modifying
    @Transactional
    @Query("""
            UPDATE MaintenanceReminderEntity m
            SET m.notificationStatus = com.vehiqon.features.carmgmt.enums.NotificationStatus.SENT,
            m.sentAt = CURRENT_TIMESTAMP,
            m.attemptCount = m.attemptCount +1
            WHERE m.id = :id
            """)
    int updateReminderNotificationStatusToSent(@Param("id") UUID reminderId);

    @Modifying
    @Transactional
    @Query("""
            UPDATE MaintenanceReminderEntity m
            SET m.notificationStatus = com.vehiqon.features.carmgmt.enums.NotificationStatus.FAILED,
            m.failedAt = CURRENT_TIMESTAMP,
            m.failureReason = :failureReason,
            m.attemptCount = m.attemptCount +1
            WHERE m.id = :id
            """)
    int updateReminderNotificationStatusToFailed(@Param("id") UUID reminderId, @Param("failureReason") String failureReason);

    @Modifying
    @Transactional
    @Query("""
            UPDATE MaintenanceReminderEntity m
            SET m.notificationStatus =com.vehiqon.features.carmgmt.enums.NotificationStatus.CANCELED
              WHERE m.id = :id
            """)
    int updateReminderNotificationStatusToCanceled(@Param("id") UUID reminderId);

}
