package com.vehiqon.features.carmgmt.repository;

import com.vehiqon.features.carmgmt.entities.MaintenanceHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MaintenanceHistoryRepository extends JpaRepository<MaintenanceHistoryEntity, UUID> {
    Optional<List<MaintenanceHistoryEntity>> findByMaintenanceReminderIdOrderByAttemptedAtDesc(UUID reminderId);

    long countByMaintenanceReminderId(UUID reminderId);

}
