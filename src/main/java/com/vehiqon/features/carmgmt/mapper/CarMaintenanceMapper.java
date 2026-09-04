package com.vehiqon.features.carmgmt.mapper;

import com.vehiqon.common.api.mapper.DateMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        uses = {DateMapper.class})
public interface CarMaintenanceMapper {

//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "carId", ignore = true)
//    @Mapping(target = "documentStatus", ignore = true)
//    @Mapping(target = "updatedAt", ignore = true)
//    @Mapping(target = "createdAt", ignore = true)
//    @Mapping(target = "notificationSent", ignore = true)
//    @Mapping(source = "notificationDate", target = "notificationDate")
//    @Mapping(source = "notificationSentAt", target = "notificationSentAt")
//    @Mapping(source = "dueDate", target = "dueDate")
//    @Mapping(source = "appointmentDate", target = "appointmentDate")
//    MaintenanceReminderEntity toEntity(MaintenanceReminderDto.CreateMaintenanceRequest maintenanceRequest);
//
//
//    @Mapping(source = "notificationDate", target = "notificationDate")
//    @Mapping(source = "notificationSentAt", target = "notificationSentAt")
//    @Mapping(source = "dueDate", target = "dueDate")
//    @Mapping(source = "appointmentDate", target = "appointmentDate")
//    MaintenanceReminderDto.MaintenanceResponse toResponse(MaintenanceReminderEntity maintenanceReminder);
//
//
//    @Mapping(target = "updatedAt", ignore = true)
//    @Mapping(target = "createdAt", ignore = true)
//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "notificationSent", ignore = true)
//    @Mapping(target = "notificationSentAt", ignore = true)
//    @Mapping(target = "carId", ignore = true)
//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    void updateEntity(MaintenanceReminderDto.UpdateMaintenanceRequest request,
//                      @MappingTarget MaintenanceReminderEntity entity);

}
