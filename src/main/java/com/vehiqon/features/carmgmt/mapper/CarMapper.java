package com.vehiqon.features.carmgmt.mapper;

import com.vehiqon.features.carmgmt.dto.CarDto;
import com.vehiqon.features.carmgmt.dto.request.CreateCarRequest;
import com.vehiqon.features.carmgmt.entities.CarEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CarMapper {

//    @Mapping(source = "brandId", target="brand")
//    @Mapping(source="modelId", target="model")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "documents", ignore = true)
    @Mapping(target = "maintenanceReminders", ignore = true)
    @Mapping(target = "serviceHistory", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "model", ignore = true)
    @Mapping(source = "purchaseDate", target = "purchaseDate")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
//    @Mapping(source = "insuranceExpiry", target = "insuranceExpiry")
    @Mapping(source = "licenseExpiry", target = "licenseExpiry")
    CarEntity toEntity(CarDto.CreateCarRequest car);

//    @Mapping(source = "brand_id", target = "brand")
//    @Mapping(source = "model_id", target = "model")
    CarDto.CarResponse toResponse(CarEntity car);

    List<CarDto.CarResponse> toResponse(List<CarEntity> cars);

    default LocalDate map(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return LocalDate.parse(value);
    }

//    public CarResponse toResponse(Car car) {
//        return CarResponse.builder()
//                .id(car.getId())
//                .nickname(car.getNickname())
//                .plateNumber(car.getPlateNumber())
//                .brand(car.getBrand().getName())
//                .model(car.getModel().getName())
//                .fuelType(car.getFuelType().name())
//                .transmission(car.getTransmission().name())
//                .vin(car.getVin())
//                .build();
//    }
}
