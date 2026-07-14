package com.vehiqon.features.carmgmt.mapper;


import com.vehiqon.common.mapper.DateMapper;
import com.vehiqon.features.carmgmt.dto.CarDto;
import com.vehiqon.features.carmgmt.entities.CarEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {DateMapper.class}
)
public interface CarMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(source = "purchaseDate", target = "purchaseDate")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(source = "licenseExpiry", target = "licenseExpiry")
    CarEntity toEntity(CarDto.CreateCarRequest car);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(source = "purchaseDate", target = "purchaseDate")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(source = "licenseExpiry", target = "licenseExpiry")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(CarDto.UpdateCarRequest request,
                      @MappingTarget CarEntity entity);


    CarDto.CarEntityResponse toResponse(CarEntity car);


    List<CarDto.CarEntityResponse> toListResponse(List<CarEntity> cars);


}



