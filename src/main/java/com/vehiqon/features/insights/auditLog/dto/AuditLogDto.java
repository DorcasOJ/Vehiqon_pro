package com.vehiqon.features.insights.auditLog.dto;

import com.vehiqon.common.api.dto.ConsumerEvent;
import com.vehiqon.common.enums.EntityEnum;
import com.vehiqon.features.insights.auditLog.enums.AuditActionType;
import com.vehiqon.features.insights.auditLog.enums.AuditStatus;
import com.vehiqon.features.insights.enums.*;

import java.util.Map;
import java.util.UUID;


public class AuditLogDto {

    private AuditLogDto(){}

    public record AuditEvent(
            UUID userId,
            AuditActionType action,
            EntityEnum entity,
            UUID entityId,
            AuditStatus status,
//            HttpServletRequest request,
//            String clientIp,
//            String userAgent,
            PublishAction publishAction,
            Map<String, Object> metadata

    ) implements ConsumerEvent {}

}
