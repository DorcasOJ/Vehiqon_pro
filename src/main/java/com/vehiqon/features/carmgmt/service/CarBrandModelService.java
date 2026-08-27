package com.vehiqon.features.carmgmt.service;

import com.vehiqon.features.carmgmt.dto.CarBrandDto;
import com.vehiqon.features.carmgmt.dto.CarModelDto;

import java.util.List;
import java.util.UUID;


public interface CarBrandModelService {
    List<CarBrandDto.CarBrandResponse> getAllBrands();
    CarBrandDto.CarBrandResponse getBrandByName(String name);
    List<CarModelDto.CarModelResponse> getModelsByBrandId(UUID brandId);

    List<CarModelDto.CarModelResponse> getAllModels();

    List<CarModelDto.CarModelResponse> getModelsByBrand(UUID brandId);

    CarModelDto.CarModelResponse getModel(UUID id);

}
