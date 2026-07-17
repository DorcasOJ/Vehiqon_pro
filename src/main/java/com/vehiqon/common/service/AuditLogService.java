package com.vehiqon.common.service;

import com.vehiqon.common.entity.AuditLogEntity;
import com.vehiqon.common.enums.EntityEnum;
import com.vehiqon.common.repository.AuditLogRepository;
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

    public void log(
            UUID userId, String action, EntityEnum entity, UUID entityId, String status,
            String description, HttpServletRequest request){
        AuditLogEntity log = AuditLogEntity.builder()
                .userId(userId)
                .action(action)
                .entity(entity.name())
                .entityId(entityId)
                .status(status)
                .description(description)
                .ipAddress(httpRequestUtils.getClientIp(request))
                .userAgent(request.getHeader("User-Agent"))
                .build();
        auditLogRepository.save(log);
    }
}
