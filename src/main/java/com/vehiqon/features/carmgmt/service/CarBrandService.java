package com.vehiqon.features.carmgmt.service;

import com.vehiqon.features.carmgmt.dto.response.CarBrandResponse;
import com.vehiqon.features.carmgmt.dto.response.CarModelResponse;

import java.util.List;
import java.util.UUID;


public interface CarBrandService {
    List<CarBrandResponse> getAllBrands();

    List<CarModelResponse> getModelsByBrand(UUID brandId);
}
