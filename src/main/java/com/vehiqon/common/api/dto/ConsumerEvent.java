package com.vehiqon.common.api.dto;

import com.vehiqon.features.insights.enums.PublishAction;

import java.util.UUID;

public interface ConsumerEvent {
//        UUID userId();
        UUID entityId();
        PublishAction publishAction();
}
