package com.vehiqon.features.insights.analytics.mapper;

import com.vehiqon.features.insights.analytics.dto.AnalyticsDto;
import com.vehiqon.features.insights.analytics.entities.FeatureSessionEntity;
import com.vehiqon.features.insights.analytics.entities.UserEventEntity;
import com.vehiqon.features.insights.analytics.entities.UserSessionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnalyticsMapper {

    @Mapping(target="id", ignore = true )
    @Mapping(target="createdAt", ignore = true)
    @Mapping(target="updatedAt", ignore = true)
    UserEventEntity toUserEntityResponse(AnalyticsDto.AnalyticsEvent event);

    @Mapping(target="publishAction", ignore = true)
    AnalyticsDto.AnalyticsEvent toAnalyticsEvents(UserEventEntity entity);

//    FeatureSessionEntity toFeatureSessionEntity()
    @Mapping(target="userId", ignore = true)
    @Mapping(target="id", ignore = true)
    @Mapping(target="createdAt", ignore = true)
    @Mapping(target="updatedAt", ignore = true)
    @Mapping(target="logoutTime", ignore = true)
    @Mapping(target="loginTime", ignore = true)
    @Mapping(target="totalDurationSeconds", ignore = true)
    @Mapping(target="lastActivityAt", ignore = true)
    UserSessionEntity toStartUserSessionEntity(AnalyticsDto.SessionContext context);


}
