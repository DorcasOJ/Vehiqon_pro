package com.vehiqon.features.carmgmt.mapper;

import com.vehiqon.common.exception.BadRequestException;
import com.vehiqon.features.carmgmt.dto.CarDto;
import com.vehiqon.features.carmgmt.dto.CarMaintenanceDto;
import com.vehiqon.features.carmgmt.entities.CarEntity;
import com.vehiqon.features.carmgmt.entities.MaintenanceReminderEntity;
import org.mapstruct.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Mapper(componentModel = "spring")
public interface CarMaintenanceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "carEntity", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "notificationSent", ignore = true)
//    @Mapping(source = "notificationDate", target = "notificationDate")
    @Mapping(source = "notificationDate", target = "notificationDate")
    @Mapping(source = "dueDate", target = "dueDate")
    @Mapping(source = "appointmentDate", target = "appointmentDate")
//    @Mapping(source = "dueDate", target = "dueDate")
    MaintenanceReminderEntity toEntity(CarMaintenanceDto.CreateMaintenanceRequest maintenanceRequest);

    @Mapping(source = "carEntity.id", target = "carId")
    @Mapping(source = "carEntity.nickname", target = "carNickname")
    @Mapping(source = "carEntity.brand.name", target = "brand")
    @Mapping(source = "carEntity.model.name", target = "model")
    @Mapping(source = "notificationDate", target = "notificationDate")
    @Mapping(source = "dueDate", target = "dueDate")
    @Mapping(source = "appointmentDate", target = "appointmentDate")
//
    CarMaintenanceDto.MaintenanceResponse toResponse(MaintenanceReminderEntity maintenanceReminder);


    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "notificationSent", ignore = true)
    @Mapping(target = "carEntity", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(CarMaintenanceDto.UpdateMaintenanceRequest request,
                      @MappingTarget MaintenanceReminderEntity entity);

    default LocalDate map(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(value,
                    DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        } catch (DateTimeParseException e) {
            throw new BadRequestException(
                    "Invalid date format. Expected dd-MM-yyyy.");
        }
    }
}
