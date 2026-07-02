package com.casdore.car_mgmt.car.mapper;

import com.casdore.car_mgmt.car.dto.response.CarResponse;
import com.casdore.car_mgmt.common.entity.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface CarMapper {

    @Mapping(source = "brand.name", target="brand")
    @Mapping(source="model.name", target="model")
    CarResponse toResponse(Car car);

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
