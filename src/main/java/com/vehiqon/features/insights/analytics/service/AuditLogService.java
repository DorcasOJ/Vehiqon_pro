package com.vehiqon.features.insights.analytics.service;

import com.vehiqon.features.insights.analytics.dto.AnalyticsDto;
import com.vehiqon.features.insights.analytics.entities.AuditLogEntity;
import com.vehiqon.features.insights.analytics.enums.AuditAction;
import com.vehiqon.features.insights.analytics.enums.AuditStatus;
import com.vehiqon.common.enums.EntityEnum;
import com.vehiqon.features.insights.analytics.repository.AuditLogRepository;
import com.vehiqon.common.utils.HttpRequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditLogService {
    private final AuditLogRepository auditLogRepository;
    private final HttpRequestUtils httpRequestUtils;

    public void log(AnalyticsDto.AuditEvent event){
        AuditLogEntity log = AuditLogEntity.builder()
                .userId(event.userId())
                .action(event.action().name())
                .entity(event.entity().name())
                .entityId(event.entityId())
                .status(event.status().name())
                .description(AuditAction.valueOf(event.action().name()).getDescription())
                .ipAddress(httpRequestUtils.getClientIp(event.request()))
                .userAgent(event.request().getHeader("User-Agent"))
                .build();
        auditLogRepository.save(log);
    }

    public void logFailure(AnalyticsDto.AuditEvent event){
        AuditLogEntity log = AuditLogEntity.builder()
                .userId(event.userId())
                .action(event.action().name())
                .entity(event.entity().name())
                .entityId(event.entityId())
                .status(AuditStatus.FAILED.name())
                .description( "FAILED: " +AuditAction.valueOf(event.action().name()).getDescription())
                .ipAddress(httpRequestUtils.getClientIp(event.request()))
                .userAgent(event.request().getHeader("User-Agent"))
                .build();
        auditLogRepository.save(log);
    }
}
