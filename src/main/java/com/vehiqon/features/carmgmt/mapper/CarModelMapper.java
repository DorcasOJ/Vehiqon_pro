package com.vehiqon.features.carmgmt.mapper;

import com.vehiqon.features.carmgmt.dto.CarDto;
import com.vehiqon.features.carmgmt.dto.CarModelDto;
import com.vehiqon.features.carmgmt.entities.CarEntity;
import com.vehiqon.features.carmgmt.entities.CarModelEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CarModelMapper {

    CarModelDto.CarModelResponse toResponse(CarModelEntity model);
}
