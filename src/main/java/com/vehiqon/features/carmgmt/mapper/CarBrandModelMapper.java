package com.vehiqon.features.carmgmt.mapper;

import com.vehiqon.common.mapper.DateMapper;
import com.vehiqon.features.carmgmt.dto.CarBrandDto;
import com.vehiqon.features.carmgmt.dto.CarModelDto;
import com.vehiqon.features.carmgmt.entities.BrandEntity;
import com.vehiqon.features.carmgmt.entities.CarModelEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = {DateMapper.class})
public interface CarBrandModelMapper {

    CarBrandDto.CarBrandResponse toCarBrandResp(BrandEntity brand);

    CarModelDto.CarModelResponse toCarModelResponse(CarModelEntity model);

}
