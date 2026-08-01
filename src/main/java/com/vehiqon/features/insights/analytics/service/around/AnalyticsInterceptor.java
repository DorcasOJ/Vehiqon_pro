package com.vehiqon.features.insights.analytics.service.around;

import com.vehiqon.features.insights.analytics.dto.AnalyticsDto;
import com.vehiqon.features.insights.analytics.dto.requestScope.AnalyticsContext;
import com.vehiqon.features.insights.analytics.enums.EntityIdSource;
import com.vehiqon.features.insights.enums.PublishAction;
import com.vehiqon.features.onboarding.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class AnalyticsInterceptor implements HandlerInterceptor {
    private final ApplicationEventPublisher publisher;
    private final AuthService authService;
    private final AnalyticsContext analyticsContext;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        if (!(handler instanceof HandlerMethod hm)) {
            return true;
        }
        AnalyticsAction annotation = hm.getMethodAnnotation(AnalyticsAction.class);
        if (annotation == null) return true;
        log.info("Analytics aspect triggered");

//        analyticsContext.setUserId(authService.getAuthenticatedUser().user().getId());
//        analyticsContext.setEventType(annotation.value());
//        analyticsContext.setPath(request.getRequestURI());
//        analyticsContext.setMethod(request.getMethod());
//        analyticsContext.setOccurredAt(LocalDateTime.now());

        UUID entityId = resolveEntityId(request, annotation);
//        analyticsContext.setEntityId(entityId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) {
        log.info("Analytics publish triggered");
        if (!(handler instanceof HandlerMethod hm)) return;
        AnalyticsAction annotation = hm.getMethodAnnotation(AnalyticsAction.class);
        if(annotation == null) return;
//        publisher.publishEvent(new AnalyticsDto.AnalyticsEvent(
//                analyticsContext.getUserId(), analyticsContext.getEventType(),
//                analyticsContext.getEntityId(), new HashMap<>(analyticsContext.getMetadata()),
//                analyticsContext.getOccurredAt(), PublishAction.ANALYTICS
//        ));

    }


    private UUID resolveEntityId(HttpServletRequest request, AnalyticsAction annotation) {
        UUID userId = authService.getAuthenticatedUser().user().getId();

        if (annotation.entityIdSource() == EntityIdSource.CURRENT_USER) {
           return userId;
        } else if (annotation.entityIdSource() == EntityIdSource.PATH_VARIABLE) {
            @SuppressWarnings("unchecked")
            Map<String, String> vars = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            if (vars != null) {
                String raw = vars.get(annotation.entityIdParam());
                if (raw != null && !raw.isBlank()) {
                    return UUID.fromString(raw);
                }
            }
        }
        return null;
    }

}
