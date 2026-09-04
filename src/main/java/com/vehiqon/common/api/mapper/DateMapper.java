package com.vehiqon.common.api.mapper;

import com.vehiqon.common.exception.BadRequestException;
import org.mapstruct.Mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Mapper(componentModel = "spring")
public interface DateMapper {

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
