package com.vehiqon.features.insights.analytics.service.around;

import com.vehiqon.features.insights.analytics.enums.EntityIdSource;
import com.vehiqon.features.insights.analytics.enums.EventType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AnalyticsAction {
    EventType value();
    EntityIdSource entityIdSource() default EntityIdSource.NONE;
    String entityIdParam() default "";

}


//@PostMapping
//@AnalyticsAction(EventType.MAINTENANCE_CREATED)
//public ResponseEntity<?> createMaintenance(...) { ... }