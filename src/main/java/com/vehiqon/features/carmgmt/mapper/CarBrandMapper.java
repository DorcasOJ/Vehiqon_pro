package com.vehiqon.features.carmgmt.mapper;

import com.vehiqon.common.exception.BadRequestException;
import com.vehiqon.features.carmgmt.dto.CarBrandDto;
import com.vehiqon.features.carmgmt.entities.BrandEntity;
import org.mapstruct.Mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Mapper(componentModel = "spring")
public interface CarBrandMapper {

    //    @Mapping(target = "updatedAt", ignore = true)
//    @Mapping(target = "createdAt", ignore = true)
//    @Mapping(target = "models", ignore = true)
//    @Mapping(target = "cars", ignore = true)
    CarBrandDto.CarBrandResponse toCarBrandResp(BrandEntity brand);


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
