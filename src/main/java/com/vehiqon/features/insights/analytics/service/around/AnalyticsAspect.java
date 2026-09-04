package com.vehiqon.features.insights.analytics.service.around;

import com.vehiqon.common.api.dto.RequestContext;
import com.vehiqon.common.enums.EntityEnum;
import com.vehiqon.common.exception.BadRequestException;
import com.vehiqon.features.insights.InsightEventPublisher;
import com.vehiqon.features.insights.analytics.dto.AnalyticsDto;
import com.vehiqon.features.insights.analytics.dto.requestScope.AnalyticsContext;
import com.vehiqon.features.insights.analytics.enums.EntityIdSource;
import com.vehiqon.features.insights.analytics.enums.EventType;
import com.vehiqon.features.insights.analytics.enums.FeatureEnum;
import com.vehiqon.features.insights.auditLog.service.RequestedMetadataService;
import com.vehiqon.features.insights.enums.PublishAction;
import com.vehiqon.features.onboarding.service.AuthService;
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
public class AnalyticsAspect {

    private final AuthService authService;
    private final HttpServletRequest request;
    private final InsightEventPublisher publisher;
    private final AnalyticsContext analyticsContext;
    private final RequestContext requestContext;
    private final RequestedMetadataService metadataService;


    @Around("@annotation(analyticsAction)")
    public Object audit(
            ProceedingJoinPoint joinPoint,
            AnalyticsAction analyticsAction
    ) throws Throwable {

        UUID userId = authService.getAuthenticatedUser().user().getId();
        EventType eventType = analyticsAction.value();
        FeatureEnum feature = analyticsAction.value().getFeature();
        EntityEnum entity = analyticsAction.value().getEntity();
        UUID entityId = resolveEntityId(analyticsAction, userId);
        AnalyticsDto.SessionContext context = requestContext.toSessionContext(userId, null);

        Map<String, Object> metadata = new HashMap<>();

        try {
            log.info("Before analysis");
            Object result = joinPoint.proceed();
            log.info("After analysis --controller");
            return result;
        } catch (Throwable e) {
            metadata.put("error", e.getMessage());
            throw new BadRequestException(e.getMessage());
        } finally {
            metadata.putAll(metadataService.createMetadata());
            metadata.putAll(analyticsContext.getMetadata());
            metadata.put("occurredAt", LocalDateTime.now());
            publisher.publish(new AnalyticsDto.AnalyticsEvent(
                    userId, eventType,entityId, metadata,LocalDateTime.now(),
                    PublishAction.ANALYTICS, context
            ));
        }

        }

        private UUID resolveEntityId(AnalyticsAction analyticsAction, UUID userId) {

            if (analyticsAction.entityIdSource() == EntityIdSource.CURRENT_USER) {
                return userId;
            } else if (analyticsAction.entityIdSource() == EntityIdSource.PATH_VARIABLE) {
                @SuppressWarnings("unchecked")
                Map<String, String> vars = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
                if (vars != null) {
                    String raw = vars.get(analyticsAction.entityIdParam());
                    if (raw != null && !raw.isBlank()) {
                        return UUID.fromString(raw);
                    }
                }
            }
            return null;
        }


}
