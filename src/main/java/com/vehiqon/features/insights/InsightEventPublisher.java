package com.vehiqon.features.insights;

import com.vehiqon.common.dto.ConsumerEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InsightEventPublisher {
    private final ApplicationEventPublisher publisher;

    public void publish(ConsumerEvent event) {
        publisher.publishEvent(event);
    }
}
