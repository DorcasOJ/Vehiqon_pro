package com.vehiqon.features.insights.analytics.mapper;

import com.vehiqon.features.insights.analytics.dto.AnalyticsDto;
import com.vehiqon.features.insights.analytics.entities.UserSessionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnalyticsMapper {

//    @Mapping(target="id", ignore = true )
//    @Mapping(target="createdAt", ignore = true)
//    @Mapping(target="updatedAt", ignore = true)
//    @Mapping(target="userSessionId", ignore = true)
//    @Mapping(target="featureSessionId", ignore = true)
//    @Mapping(target="feature", ignore = true)
//    @Mapping(target="entityType", ignore = true)
//    UserEventEntity toUserEntityResponse(AnalyticsDto.AnalyticsEvent event);

//    @Mapping(target="publishAction", ignore = true)
//    @Mapping(target="sessionData", ignore = true)
//    AnalyticsDto.AnalyticsEvent toAnalyticsEvents(UserEventEntity entity);

//    FeatureSessionEntity toFeatureSessionEntity()
    @Mapping(target="id", ignore = true)
    @Mapping(target="createdAt", ignore = true)
    @Mapping(target="updatedAt", ignore = true)
    @Mapping(target="logoutAt", ignore = true)
    @Mapping(target="loginAt", ignore = true)
    @Mapping(target="durationSeconds", ignore = true)
    @Mapping(target="lastActivityAt", ignore = true)
    @Mapping(target="active", ignore = true)
    UserSessionEntity toStartUserSessionEntity(AnalyticsDto.SessionContext context);


}
