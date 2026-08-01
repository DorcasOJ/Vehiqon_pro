package com.vehiqon.features.insights.auditLog.service.around;

import com.vehiqon.common.enums.EntityEnum;
import com.vehiqon.features.insights.analytics.enums.EntityIdSource;
import com.vehiqon.features.insights.auditLog.enums.AuditActionType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuditAction {
    AuditActionType value();
    EntityEnum entityType() default EntityEnum.NONE;
    EntityIdSource entityIdSource() default EntityIdSource.NONE;
    String entityIdParam() default "";
}
