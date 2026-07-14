package com.vehiqon.features.carmgmt.repository;

import com.vehiqon.features.carmgmt.dto.CarMaintenanceDto;
import com.vehiqon.features.carmgmt.dto.response.MaintenanceReminderResponse;
import com.vehiqon.features.carmgmt.entities.MaintenanceReminderEntity;
import com.vehiqon.features.carmgmt.enums.MaintenanceStatus;
import com.vehiqon.features.carmgmt.enums.MaintenanceType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarMaintenanceRepository extends JpaRepository<MaintenanceReminderEntity, UUID> {

    List<MaintenanceReminderEntity> findAllByCarId(UUID carId);

    boolean existsByCarIdAndAppointmentDateAndAppointmentTime(
            UUID carId,
            LocalDate appointmentDate,
            LocalTime appointmentTime
    );

    List<MaintenanceReminderEntity>
    findAllByNotificationSentFalseAndNotificationDateLessThanEqual(
            LocalDate notificationDate
    );

    List<MaintenanceReminderEntity> findAllByStatus(
            MaintenanceStatus status
    );

//    List<MaintenanceReminderEntity> findAllByUserIdAndStatus(
//            UUID userId,
//            MaintenanceStatus status
//    );

    @Query("""
        SELECT mr
        FROM MaintenanceReminderEntity mr
        JOIN CarEntity c ON mr.carId = c.id
        WHERE c.userId = :userId
          AND mr.status = :status
    """)
    List<MaintenanceReminderEntity> findAllByUserIdAndStatus(
            @Param("userId") UUID userId,
            @Param("status") MaintenanceStatus status
    );


    @Query("""
        SELECT m
        FROM MaintenanceReminderEntity m
        JOIN CarEntity c ON m.carId = c.id
        WHERE m.id = :id
          AND c.userId = :userId
    """)
    Optional<MaintenanceReminderEntity> findByIdAndUserId(
            UUID id,
            UUID userId
    );

    @Query("""
    SELECT new com.vehiqon.features.carmgmt.dto.response.MaintenanceReminderResponse(
        mr.id,
         mr.title,
        mr.dueDate,
        mr.workshop,
        mr.type,
        mr.appointmentDate,
        mr.appointmentTime,
        mr.notificationDate,
        mr.notificationSentAt,
        mr.odometer,
        mr.estimatedCost,
        mr.notes,
    
        u.id,
        u.firstName,
        u.lastName,
        u.email,
        u.phoneNumber,
       
        c.id,
        c.nickname,
        cb.name,
        cm.name,
        c.plateNumber
    )
    FROM MaintenanceReminderEntity mr
    JOIN CarEntity c
        ON mr.carId = c.id
    JOIN BrandEntity cb
        ON c.carBrandId = cb.id
    JOIN CarModelEntity cm
        ON c.carModelId = cm.id
    JOIN UserEntity u
        ON c.userId = u.id
       
    WHERE mr.notificationSent = false
    AND mr.notificationDate <= :today
    """)
    Optional<List<MaintenanceReminderResponse>> findDueNotifications(LocalDate today);


    @Query("""
    SELECT new com.vehiqon.features.carmgmt.dto.response.MaintenanceReminderResponse(
        mr.id,
         mr.title,
        mr.dueDate,
        mr.workshop,
        mr.type,
        mr.appointmentDate,
        mr.appointmentTime,
        mr.notificationDate,
        mr.notificationSentAt,
        mr.odometer,
        mr.estimatedCost,
        mr.notes,
    
        u.id,
        u.firstName,
        u.lastName,
        u.email,
        u.phoneNumber,
       
        c.id,
        c.nickname,
        cb.name,
        cm.name,
        c.plateNumber
    )
    FROM MaintenanceReminderEntity mr
    JOIN CarEntity c
        ON mr.carId = c.id
    JOIN BrandEntity cb
        ON c.carBrandId = cb.id
    JOIN CarModelEntity cm
        ON c.carModelId = cm.id
    JOIN UserEntity u
        ON c.userId = u.id
       
    WHERE c.userId = :userId 
    """)
    Optional<List<MaintenanceReminderResponse>> findAllMaintenanceReminderByUserId(UUID userId);

    @Query("""
    SELECT new com.vehiqon.features.carmgmt.dto.response.MaintenanceReminderResponse(
        mr.id,
        mr.title,
        mr.dueDate,
        mr.workshop,
        mr.type,
        mr.appointmentDate,
        mr.appointmentTime,
        mr.notificationDate,
        mr.notificationSentAt,
        mr.odometer,
        mr.estimatedCost,
        mr.notes,
    
        u.id,
        u.firstName,
        u.lastName,
        u.email,
        u.phoneNumber,
       
        c.id,
        c.nickname,
        cb.name,
        cm.name,
        c.plateNumber
    )
    FROM MaintenanceReminderEntity mr
    JOIN CarEntity c
        ON mr.carId = c.id
    JOIN BrandEntity cb
        ON c.carBrandId = cb.id
    JOIN CarModelEntity cm
        ON c.carModelId = cm.id
    JOIN UserEntity u
        ON c.userId = u.id
       
    WHERE c.userId = :userId
    AND mr.id = :maintenanceReminderId
    """)
    Optional<MaintenanceReminderResponse> findMaintenanceReminderByUserId(UUID userId, UUID maintenanceReminderId);


    @Query("""
    SELECT new com.vehiqon.features.carmgmt.dto.response.MaintenanceReminderResponse(
        mr.id,
        mr.title,
        mr.dueDate,
        mr.workshop,
        mr.type,
        mr.appointmentDate,
        mr.appointmentTime,
        mr.notificationDate,
        mr.notificationSentAt,
        mr.odometer,
        mr.estimatedCost,
        mr.notes,
    
        u.id,
        u.firstName,
        u.lastName,
        u.email,
        u.phoneNumber,
       
        c.id,
        c.nickname,
        cb.name,
        cm.name,
        c.plateNumber
    )
    FROM MaintenanceReminderEntity mr
    JOIN CarEntity c
        ON mr.carId = c.id
    JOIN BrandEntity cb
        ON c.carBrandId = cb.id
    JOIN CarModelEntity cm
        ON c.carModelId = cm.id
    JOIN UserEntity u
        ON c.userId = u.id
       
    WHERE c.id = :carId
    """)
    Optional<List<MaintenanceReminderResponse>> findMaintenanceReminderByCarId(UUID carId);



    @Modifying
    @Transactional
    @Query("""
            UPDATE MaintenanceReminderEntity m
            SET m.notificationSent = true,
                m.notificationSentAt = CURRENT_TIMESTAMP
            WHERE m.id = id
            """)
    void markNotificationSent(UUID id);

}
