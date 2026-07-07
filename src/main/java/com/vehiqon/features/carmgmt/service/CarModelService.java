package com.vehiqon.features.carmgmt.service;

import com.vehiqon.features.carmgmt.dto.CarModelDto;
import com.vehiqon.features.carmgmt.repository.CarBrandRepository;
import com.vehiqon.features.carmgmt.repository.ModelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface CarModelService {

    List<CarModelDto.CarModelResponse> getAllModels();

    List<CarModelDto.CarModelResponse> getModelsByBrand(UUID brandId);

    CarModelDto.CarModelResponse getModel(UUID id);

}
