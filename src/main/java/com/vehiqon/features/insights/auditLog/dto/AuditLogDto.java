package com.vehiqon.features.insights.auditLog.dto;

import com.vehiqon.common.dto.ConsumerEvent;
import com.vehiqon.common.enums.EntityEnum;
import com.vehiqon.features.insights.auditLog.enums.AuditAction;
import com.vehiqon.features.insights.auditLog.enums.AuditStatus;
import com.vehiqon.features.insights.enums.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;


public class AuditLogDto {

    private AuditLogDto(){}

    public record AuditEvent(
            UUID userId,
            AuditAction action,
            EntityEnum entity,
            UUID entityId,
            AuditStatus status,
           HttpServletRequest request,
            PublishAction publishAction

    ) implements ConsumerEvent {}

}
