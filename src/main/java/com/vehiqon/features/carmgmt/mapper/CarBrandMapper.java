package com.vehiqon.features.carmgmt.mapper;

import com.vehiqon.features.carmgmt.dto.CarBrandDto;
import com.vehiqon.features.carmgmt.entities.BrandEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CarBrandMapper {

    //    @Mapping(target = "updatedAt", ignore = true)
//    @Mapping(target = "createdAt", ignore = true)
//    @Mapping(target = "models", ignore = true)
//    @Mapping(target = "cars", ignore = true)
    CarBrandDto.CarBrandResponse toCarBrandResp(BrandEntity brand);

}
