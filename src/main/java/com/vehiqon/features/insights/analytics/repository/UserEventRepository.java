package com.vehiqon.features.insights.analytics.repository;

import com.vehiqon.features.insights.analytics.entities.UserEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserEventRepository extends JpaRepository<UserEventEntity, UUID> {
}
