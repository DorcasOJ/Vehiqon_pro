package com.vehiqon.features.carmgmt.repository;

import com.vehiqon.features.carmgmt.entities.CarMaintenanceEntity;
import com.vehiqon.features.carmgmt.enums.MaintenanceType;
import com.vehiqon.features.carmgmt.enums.MaintenanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarMaintenanceRepository extends JpaRepository<CarMaintenanceEntity, UUID> {

    Optional<List<CarMaintenanceEntity>> findByCarId(UUID carId);
    Optional<List<CarMaintenanceEntity>> findByUserId(UUID carId);
    Optional<List<CarMaintenanceEntity>> findByMaintenanceStatus(MaintenanceStatus maintenanceStatus);
    Optional<List<CarMaintenanceEntity>> findByMaintenanceType(MaintenanceType maintenanceType);
    Optional<List<CarMaintenanceEntity>> findByAppointmentDate(LocalDate appointmentDate);
    Optional<List<CarMaintenanceEntity>> findByAppointmentDateBetween(LocalDate start, LocalDate end);



//    @Query("""
//        SELECT m
//        FROM MaintenanceReminderEntity m
//        JOIN CarEntity c ON m.carId = c.id
//        WHERE m.id = :id
//          AND c.userId = :userId
//    """)
//    Optional<MaintenanceReminderEntity> findByIdAndUserId(
//            UUID id,
//            UUID userId
//    );

//    @Query("""
//        SELECT mr
//        FROM MaintenanceReminderEntity mr
//        JOIN CarEntity c ON mr.carId = c.id
//        WHERE c.userId = :userId
//          AND mr.status = :status
//    """)
//    List<MaintenanceReminderEntity> findAllByUserIdAndStatus(
//            @Param("userId") UUID userId,
//            @Param("status") MaintenanceStatus status
//    );
//
//
//
//    @Query("""
//    SELECT new com.vehiqon.features.carmgmt.dto.response.MaintenanceReminderResponse(
//        mr.id,
//         mr.title,
//        mr.dueDate,
//        mr.workshop,
//        mr.type,
//        mr.appointmentDate,
//        mr.appointmentTime,
//        mr.notificationDate,
//        mr.notificationSentAt,
//        mr.odometer,
//        mr.estimatedCost,
//        mr.notes,
//
//        u.id,
//        u.firstName,
//        u.lastName,
//        u.email,
//        u.phoneNumber,
//
//        c.id,
//        c.nickname,
//        cb.name,
//        cm.name,
//        c.plateNumber
//    )
//    FROM MaintenanceReminderEntity mr
//    JOIN CarEntity c
//        ON mr.carId = c.id
//    JOIN BrandEntity cb
//        ON c.carBrandId = cb.id
//    JOIN CarModelEntity cm
//        ON c.carModelId = cm.id
//    JOIN UserEntity u
//        ON c.userId = u.id
//
//    WHERE c.userId = :userId
//    """)
//    Optional<List<MaintenanceReminderResponse>> findAllMaintenanceReminderByUserId(UUID userId);
//
//    @Query("""
//    SELECT new com.vehiqon.features.carmgmt.dto.response.MaintenanceReminderResponse(
//        mr.id,
//        mr.title,
//        mr.dueDate,
//        mr.workshop,
//        mr.type,
//        mr.appointmentDate,
//        mr.appointmentTime,
//        mr.notificationDate,
//        mr.notificationSentAt,
//        mr.odometer,
//        mr.estimatedCost,
//        mr.notes,
//
//        u.id,
//        u.firstName,
//        u.lastName,
//        u.email,
//        u.phoneNumber,
//
//        c.id,
//        c.nickname,
//        cb.name,
//        cm.name,
//        c.plateNumber
//    )
//    FROM MaintenanceReminderEntity mr
//    JOIN CarEntity c
//        ON mr.carId = c.id
//    JOIN BrandEntity cb
//        ON c.carBrandId = cb.id
//    JOIN CarModelEntity cm
//        ON c.carModelId = cm.id
//    JOIN UserEntity u
//        ON c.userId = u.id
//
//    WHERE c.userId = :userId
//    AND mr.id = :maintenanceReminderId
//    """)
//    Optional<MaintenanceReminderResponse> findMaintenanceReminderByUserId(UUID userId, UUID maintenanceReminderId);
//
//
//    @Query("""
//    SELECT new com.vehiqon.features.carmgmt.dto.response.MaintenanceReminderResponse(
//        mr.id,
//        mr.title,
//        mr.dueDate,
//        mr.workshop,
//        mr.type,
//        mr.appointmentDate,
//        mr.appointmentTime,
//        mr.notificationDate,
//        mr.notificationSentAt,
//        mr.odometer,
//        mr.estimatedCost,
//        mr.notes,
//
//        u.id,
//        u.firstName,
//        u.lastName,
//        u.email,
//        u.phoneNumber,
//
//        c.id,
//        c.nickname,
//        cb.name,
//        cm.name,
//        c.plateNumber
//    )
//    FROM MaintenanceReminderEntity mr
//    JOIN CarEntity c
//        ON mr.carId = c.id
//    JOIN BrandEntity cb
//        ON c.carBrandId = cb.id
//    JOIN CarModelEntity cm
//        ON c.carModelId = cm.id
//    JOIN UserEntity u
//        ON c.userId = u.id
//
//    WHERE c.id = :carId
//    """)
//    Optional<List<MaintenanceReminderResponse>> findMaintenanceReminderByCarId(UUID carId);
//
//
//
//    @Modifying
//    @Transactional
//    @Query("""
//            UPDATE MaintenanceReminderEntity m
//            SET m.notificationSent = true,
//                m.notificationSentAt = CURRENT_TIMESTAMP
//            WHERE m.id = id
//            """)
//    void markNotificationSent(UUID id);

}


//SELECT *
//FROM maintenance_reminders
//WHERE scheduled_at <= NOW()
//AND status = 'PENDING'
