package com.vehiqon.features.carmgmt.service.impl;

import com.vehiqon.common.exception.ResourceNotFoundException;
import com.vehiqon.features.carmgmt.dto.CarModelDto;
import com.vehiqon.features.carmgmt.entities.BrandEntity;
import com.vehiqon.features.carmgmt.mapper.CarModelMapper;
import com.vehiqon.features.carmgmt.repository.CarBrandRepository;
import com.vehiqon.features.carmgmt.repository.CarModelRepository;
import com.vehiqon.features.carmgmt.service.CarModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarModelServiceImpl implements CarModelService {

    private final CarModelRepository carModelRepository;
    private final CarBrandRepository brandRepository;
    private final CarModelMapper carModelMapper;

    @Override
    public List<CarModelDto.CarModelResponse> getAllModels() {
        return carModelRepository.findAll().stream()
                .map(carModelMapper::toResponse)
                .toList();
    }

    @Override
    public List<CarModelDto.CarModelResponse> getModelsByBrand(UUID brandId) {
        BrandEntity brand = brandRepository.findById(brandId).orElseThrow(() -> new ResourceNotFoundException("Brand not found"));
        return carModelRepository.findAllByBrand(brand).stream()
                .map(carModelMapper::toResponse)
                .toList();
    }

    @Override
    public CarModelDto.CarModelResponse getModel(UUID id) {
        return carModelMapper.toResponse(
                carModelRepository.findById(id).orElseThrow(() ->
                        new ResourceNotFoundException("Model not found"))
        );
    }
}
