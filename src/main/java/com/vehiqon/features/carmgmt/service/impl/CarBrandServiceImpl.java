package com.vehiqon.features.carmgmt.service.impl;

import com.vehiqon.common.exception.ResourceNotFoundException;
import com.vehiqon.features.carmgmt.dto.response.CarBrandResponse;
import com.vehiqon.features.carmgmt.dto.response.CarModelResponse;
import com.vehiqon.features.carmgmt.entities.BrandEntity;
import com.vehiqon.features.carmgmt.repository.CarBrandRepository;
import com.vehiqon.features.carmgmt.repository.CarModelRepository;
import com.vehiqon.features.carmgmt.service.CarBrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarBrandServiceImpl implements CarBrandService {

    private final CarBrandRepository brandRepository;
    private final CarModelRepository modelRepository;

    @Override
    public List<CarBrandResponse> getAllBrands() {
        return brandRepository.findAll()
                .stream()
                .map(brand -> new CarBrandResponse(brand.getId(), brand.getName()))
                .toList();
    }

    @Override
    public List<CarModelResponse> getModelsByBrand(UUID brandId) {
        BrandEntity brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found"));
        return modelRepository.findByBrandId(brand.getId())
                .stream()
                .map(model -> new CarModelResponse(model.getId(), model.getName()))
                .toList();

    }
}
