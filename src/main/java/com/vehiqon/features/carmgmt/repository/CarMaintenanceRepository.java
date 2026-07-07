package com.vehiqon.features.carmgmt.repository;

import com.vehiqon.features.carmgmt.entities.CarEntity;
import com.vehiqon.features.carmgmt.entities.MaintenanceReminderEntity;
import com.vehiqon.features.onboarding.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarMaintenanceRepository extends JpaRepository<MaintenanceReminderEntity, UUID> {
    List<MaintenanceReminderEntity> findAllByUser(UserEntity user);

    List<MaintenanceReminderEntity> findAllByCarEntity(CarEntity car);

//    List<MaintenanceReminderEntity> findAllByUserAndStatus(
//            UserEntity user,
//            ReminderStatus status
//    );

    Optional<MaintenanceReminderEntity> findByIdAndUser(UUID id, UserEntity user);

    List<MaintenanceReminderEntity> findAllByCarEntityAndUser(CarEntity car, UserEntity user);

    boolean existsByCarEntityAndAppointmentDateAndAppointmentTime(
            CarEntity car,
            LocalDate appointmentDate,
            LocalTime appointmentTime
    );

    List<MaintenanceReminderEntity> findAllByNotificationSentFalseAndNotificationDateLessThanEqual(LocalDate dateTime);
}
