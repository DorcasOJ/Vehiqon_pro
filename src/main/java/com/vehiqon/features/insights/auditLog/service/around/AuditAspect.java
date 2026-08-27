package com.vehiqon.features.insights.auditLog.service.around;

import com.vehiqon.common.enums.EntityEnum;
import com.vehiqon.common.exception.BadRequestException;
import com.vehiqon.common.utils.HttpRequestUtils;
import com.vehiqon.features.insights.InsightEventPublisher;
import com.vehiqon.features.insights.analytics.enums.EntityIdSource;
import com.vehiqon.features.insights.auditLog.dto.AuditLogDto;
import com.vehiqon.features.insights.auditLog.dto.requestScope.AuditContext;
import com.vehiqon.features.insights.auditLog.enums.AuditActionType;
import com.vehiqon.features.insights.auditLog.enums.AuditStatus;
import com.vehiqon.features.insights.auditLog.service.RequestedMetadataService;
import com.vehiqon.features.insights.enums.PublishAction;
import com.vehiqon.features.onboarding.service.AuthService;
import com.vehiqon.security.model.CustomerUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {
    private final AuthService authService;
    private final HttpServletRequest request;
    private final InsightEventPublisher publisher;
    private final AuditContext auditContext;
    private final RequestedMetadataService metadataService;
    private final HttpRequestUtils httpRequestUtils;

    @Around("@annotation(auditAction)")
    public Object audit(
            ProceedingJoinPoint joinPoint,
            AuditAction auditAction
    ) throws Throwable {

        CustomerUserDetails authenticatedUser = authService.getAuthenticatedUser();
        UUID userId = authenticatedUser.user().getId();
        UUID jti = authenticatedUser.jti();
        Map<String, Object> metadata = new HashMap<>();
        AuditActionType action = auditAction.value();

        EntityEnum entity = auditAction.entityType();
        UUID entityId = resolveEntityId(auditAction, userId);
        AuditStatus status = AuditStatus.SUCCESS;
        try {
            log.info("Before audit");
            Object result = joinPoint.proceed();
            log.info("After audit -- controller");
            return result;
        } catch (Throwable e) {
            status = AuditStatus.FAILED;
            metadata.put("error", e.getMessage());
            throw new BadRequestException(e.getMessage());
        } finally {
            metadata.putAll(metadataService.createMetadata());
            metadata.putAll(auditContext.getMetadata());
            metadata.put("jti",jti);

            publisher.publish(new AuditLogDto.AuditEvent(
                    userId, action , entity, entityId,
                    status, PublishAction.AUDIT_LOG, metadata
            ));
        }

    }

    private UUID resolveEntityId(AuditAction auditAction, UUID userId) {

        if (auditAction.entityIdSource() == EntityIdSource.CURRENT_USER) {
            return userId;
        } else if (auditAction.entityIdSource() == EntityIdSource.PATH_VARIABLE) {
            @SuppressWarnings("unchecked")
            Map<String, String> vars = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            if (vars != null) {
                String raw = vars.get(auditAction.entityIdParam());
                if (raw != null && !raw.isBlank()) {
                    return UUID.fromString(raw);
                }
            }
        }
        return null;
    }

}
