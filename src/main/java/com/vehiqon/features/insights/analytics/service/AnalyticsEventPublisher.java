package com.vehiqon.features.insights.analytics.service;

import com.vehiqon.features.insights.analytics.dto.AnalyticsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnalyticsEventPublisher {
    private final ApplicationEventPublisher publisher;

    public void publish(AnalyticsDto.event event) {
        publisher.publishEvent(event);
    }
}
