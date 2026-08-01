package com.vehiqon.features.insights.auditLog.service;

import com.vehiqon.features.insights.auditLog.entity.AuditLogEntity;
import com.vehiqon.features.insights.auditLog.enums.AuditActionType;
import com.vehiqon.features.insights.auditLog.enums.AuditStatus;
import com.vehiqon.features.insights.auditLog.dto.AuditLogDto;
import com.vehiqon.features.insights.auditLog.repository.AuditLogRepository;
import com.vehiqon.common.utils.HttpRequestUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditLogService {
    private final AuditLogRepository auditLogRepository;
    private final HttpRequestUtils httpRequestUtils;

    public void log(AuditLogDto.AuditEvent event){
        AuditLogEntity log = AuditLogEntity.builder()
                .userId(event.userId())
                .action(event.action().name())
                .entity(event.entity().name())
                .entityId(event.entityId())
                .status(event.status().name())
                .metadata(event.metadata())
                .description(AuditActionType.valueOf(event.action().name()).getDescription())
//                .ipAddress(event.clientIp())
//                .userAgent(event.userAgent())
                .build();
        auditLogRepository.save(log);
    }

    public void logFailure(AuditLogDto.AuditEvent event){
        AuditLogEntity log = AuditLogEntity.builder()
                .userId(event.userId())
                .action(event.action().name())
                .entity(event.entity().name())
                .entityId(event.entityId())
                .status(AuditStatus.FAILED.name())
                .metadata(event.metadata())
                .description( "FAILED: " + AuditActionType.valueOf(event.action().name()).getDescription())
//                .ipAddress(httpRequestUtils.getClientIp(event.request()))
//                .userAgent(event.request().getHeader("User-Agent"))
                .build();
        auditLogRepository.save(log);
    }
}
