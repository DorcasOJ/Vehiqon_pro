package com.vehiqon.features.carmgmt.mapper;

import com.vehiqon.common.exception.ResourceNotFoundException;
import com.vehiqon.features.carmgmt.dto.CarBrandDto;
import com.vehiqon.features.carmgmt.dto.CarDto;
import com.vehiqon.features.carmgmt.dto.CarModelDto;
import com.vehiqon.features.carmgmt.repository.CarBrandRepository;
import com.vehiqon.features.carmgmt.repository.ModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CarResponseMapper {
    private final CarBrandRepository carBrandRepository;
    private final ModelRepository modelRepository;
    private final CarBrandModelMapper carBrandModelMapper;

        public CarDto.CarResponse toResponse(CarDto.CarEntityResponse car) {
            CarBrandDto.CarBrandResponse carBrand = carBrandModelMapper.toCarBrandResp(
                    carBrandRepository.findById(car.carBrandId()).orElseThrow(() ->
                            new ResourceNotFoundException("Car Brand not found")));

            CarModelDto.CarModelResponse carModel = carBrandModelMapper.toCarModelResponse(
                    modelRepository.findById(car.carModelId()).orElseThrow(
                            () -> new ResourceNotFoundException("Car Model not found")
                    ));
            return new CarDto.CarResponse(car.id(), car.nickname(), car.vin(), car.plateNumber(),
                car.color(), car.year(), car.engineNumber(), car.fuelType(), car.transmission(),
                car.odometer(), car.purchaseDate(), car.licenseExpiry(), car.status(), carBrand, carModel );

    }

}
