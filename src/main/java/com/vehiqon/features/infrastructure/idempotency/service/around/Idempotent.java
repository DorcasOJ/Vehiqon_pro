package com.vehiqon.features.infrastructure.idempotency.service.around;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Idempotent {
    /**
     * Duration in seconds for keeping the idempotency lock/record active in redis
     * @return a default of 120 seconds (2 minutes)
     */
    int expireInSeconds() default 120;
}
