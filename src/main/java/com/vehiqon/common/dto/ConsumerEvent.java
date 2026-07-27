package com.vehiqon.common.dto;

import com.vehiqon.features.insights.enums.PublishAction;

import java.util.UUID;

public interface ConsumerEvent {
//        UUID userId();
        UUID entityId();
        PublishAction publishAction();
}
