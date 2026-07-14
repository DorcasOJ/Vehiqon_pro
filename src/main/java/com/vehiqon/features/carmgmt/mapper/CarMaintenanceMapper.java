package com.vehiqon.features.carmgmt.mapper;

import com.vehiqon.common.exception.BadRequestException;
import com.vehiqon.common.mapper.DateMapper;
import com.vehiqon.features.carmgmt.dto.CarDto;
import com.vehiqon.features.carmgmt.dto.CarMaintenanceDto;
import com.vehiqon.features.carmgmt.entities.CarEntity;
import com.vehiqon.features.carmgmt.entities.MaintenanceReminderEntity;
import org.mapstruct.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Mapper(componentModel = "spring",
        uses = {DateMapper.class})
public interface CarMaintenanceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "carId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "notificationSent", ignore = true)
    @Mapping(source = "notificationDate", target = "notificationDate")
    @Mapping(source = "notificationSentAt", target = "notificationSentAt")
    @Mapping(source = "dueDate", target = "dueDate")
    @Mapping(source = "appointmentDate", target = "appointmentDate")
    MaintenanceReminderEntity toEntity(CarMaintenanceDto.CreateMaintenanceRequest maintenanceRequest);


    @Mapping(source = "notificationDate", target = "notificationDate")
    @Mapping(source = "notificationSentAt", target = "notificationSentAt")
    @Mapping(source = "dueDate", target = "dueDate")
    @Mapping(source = "appointmentDate", target = "appointmentDate")
    CarMaintenanceDto.MaintenanceResponse toResponse(MaintenanceReminderEntity maintenanceReminder);


    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "notificationSent", ignore = true)
    @Mapping(target = "notificationSentAt", ignore = true)
    @Mapping(target = "carId", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(CarMaintenanceDto.UpdateMaintenanceRequest request,
                      @MappingTarget MaintenanceReminderEntity entity);

}
