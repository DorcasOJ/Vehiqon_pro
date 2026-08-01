package com.vehiqon.features.insights.analytics.service;

import com.vehiqon.features.insights.InsightEventPublisher;
import com.vehiqon.features.insights.analytics.dto.AnalyticsDto;
import com.vehiqon.features.insights.analytics.entities.UserEventEntity;
import com.vehiqon.features.insights.analytics.enums.EventType;
import com.vehiqon.features.insights.analytics.enums.FeatureEnum;
import com.vehiqon.features.insights.analytics.mapper.AnalyticsMapper;
import com.vehiqon.features.insights.analytics.repository.UserEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticEventService {
    private final InsightEventPublisher publisher;
    private final AnalyticsMapper userEventMapper;
    private final UserEventRepository userEventRepository;

    public void saveEvent(AnalyticsDto.AnalyticsEvent event, UUID userSessionId,
                          UUID featureSessionId) {
//        UserEventEntity userEntityResponse = userEventMapper.toUserEntityResponse(event);
        UserEventEntity userEntityResponse = UserEventEntity.builder()
                .userId(event.userId())
                .userSessionId(userSessionId)
                .featureSessionId(featureSessionId)
                .eventType(event.eventType())
                .entityType(event.eventType().getEntity())
                .feature(event.eventType().getFeature())
                .metadata(event.metadata() == null || event.metadata().isEmpty() ?
                        buildMetadata(event) : event.metadata() )
                .entityId(event.entityId())
                .occurredAt(event.occurredAt())
                .build();

        userEventRepository.save(userEntityResponse);
    }

    public Map<String, Object> buildMetadata(AnalyticsDto.AnalyticsEvent event) {
        Map<String, Object> metadata = new HashMap<>();
//        metadata.put("path", event.path() );
//        metadata.put("method", event.method() );
        metadata.put("occurredAt", event.occurredAt() );
        return metadata;
    }


//    to be used by dashboards.
    public List<UserEventEntity> searchEvents(
            UUID userId, FeatureEnum feature, EventType type, LocalDateTime from, LocalDateTime to
    ) {
        Optional<List<UserEventEntity>> searchEventOpts = userEventRepository.findByUserIdAndFeatureAndEventTypeAndOccurredAtBetween(
                userId, feature, type, from, to
        );
        return searchEventOpts.orElseGet(List::of);

    }
}
//metadatas, what exactly happened
//{
//        "query": "Toyota Camry",
//        "results": 18
//        }
//        {
//        "amount": 5000,
//        "currency": "NGN",
//        "provider": "Nomba"
//        }
//        {
//        "maintenanceType": "OIL_CHANGE",
//        "workshop": "Toyota Ikeja"
//        }