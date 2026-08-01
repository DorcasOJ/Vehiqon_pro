package com.vehiqon.features.insights.auditLog.repository;

import com.vehiqon.features.insights.auditLog.entity.AuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuditLogRepository extends JpaRepository<AuditLogEntity, UUID> {
}
