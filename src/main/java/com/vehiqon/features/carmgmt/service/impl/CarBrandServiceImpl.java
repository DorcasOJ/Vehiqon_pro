package com.vehiqon.features.carmgmt.service.impl;

import com.vehiqon.common.exception.ResourceNotFoundException;
import com.vehiqon.features.carmgmt.dto.CarBrandDto;
import com.vehiqon.features.carmgmt.dto.CarModelDto;
import com.vehiqon.features.carmgmt.entities.BrandEntity;
import com.vehiqon.features.carmgmt.mapper.CarBrandModelMapper;
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
    private final CarBrandModelMapper carBrandModelMapper;

    @Override
    public List<CarBrandDto.CarBrandResponse> getAllBrands() {
        return brandRepository.findAll()
                .stream()
                .map(brand -> new CarBrandDto.CarBrandResponse(brand.getId(), brand.getName()))
                .toList();
    }

    @Override
    public CarBrandDto.CarBrandResponse getBrandByName(String name) {
        BrandEntity brand = brandRepository.findByNameIgnoreCase(name).orElseThrow(() -> new ResourceNotFoundException("Brand name not found"));
        return carBrandModelMapper.toCarBrandResp(brand);

    }

    @Override
    public List<CarModelDto.CarModelResponse> getModelsByBrandId(UUID brandId) {
        BrandEntity brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found"));
        return modelRepository.findAllByCarBrandId(brand.getId())
                .stream()
                .map(model -> new CarModelDto.CarModelResponse(model.getId(), model.getName()))
                .toList();

    }
}
