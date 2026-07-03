package com.vehiqon.features.carmgmt.mapper;

import com.vehiqon.features.carmgmt.dto.request.CreateCarRequest;
import com.vehiqon.features.carmgmt.dto.response.CarResponse;
import com.vehiqon.features.carmgmt.entities.CarEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CarMapper {

    @Mapping(source = "brandId", target="brand")
    @Mapping(source="modelId", target="model")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    CarEntity toEntity(CreateCarRequest car);

    @Mapping(source = "brand.name", target = "brand")
    @Mapping(source = "model.name", target = "model")
    CarResponse toResponse(CarEntity car);

    List<CarResponse> toResponse(List<CarEntity> cars);

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
