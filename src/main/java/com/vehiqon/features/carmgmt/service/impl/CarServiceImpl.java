package com.vehiqon.features.carmgmt.service.impl;

import com.vehiqon.common.exception.BadRequestException;
import com.vehiqon.common.exception.ResourceNotFoundException;
import com.vehiqon.features.carmgmt.dto.request.CreateCarRequest;
import com.vehiqon.features.carmgmt.dto.response.CarResponse;
import com.vehiqon.features.carmgmt.entities.BrandEntity;
import com.vehiqon.features.carmgmt.entities.CarEntity;
import com.vehiqon.features.carmgmt.entities.CarModelEntity;
import com.vehiqon.features.carmgmt.mapper.CarMapper;
import com.vehiqon.features.carmgmt.repository.CarBrandRepository;
import com.vehiqon.features.carmgmt.repository.CarModelRepository;
import com.vehiqon.features.carmgmt.repository.CarRepository;
import com.vehiqon.features.carmgmt.service.CarService;
import com.vehiqon.features.onboarding.entity.UserEntity;
import com.vehiqon.features.onboarding.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final CarBrandRepository brandRepository;
    private final CarModelRepository modelRepository;
    private final UserRepository userRepository;
    private final CarMapper carMapper;

    public CarServiceImpl(CarRepository carRepository, CarBrandRepository brandRepository, CarModelRepository modelRepository, UserRepository userRepository, CarMapper carMapper) {
        this.carRepository = carRepository;
        this.brandRepository = brandRepository;
        this.modelRepository = modelRepository;
        this.userRepository = userRepository;
        this.carMapper = carMapper;
    }

    @Override
    public CarResponse registerCar(UserEntity authUser, CreateCarRequest request) {
        UserEntity user = userRepository.findById(authUser.getId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        BrandEntity brand = brandRepository.findById(request.modelId()).orElseThrow(() -> new ResourceNotFoundException("Car brand not found"));
        CarModelEntity model = modelRepository.findById(request.modelId()).orElseThrow(() -> new ResourceNotFoundException("Model not found"));
        if(!model.getBrand().getId().equals(brand.getId())) {
            throw new BadRequestException("Selected model does not belong to selected brand");
        }
        if(carRepository.existsByVin(request.vin()).isPresent()) {
            throw new BadRequestException("VIN already exists");
        }
        if(carRepository.existsByPlateNumber(request.plateNumber()).isPresent()) {
            throw new BadRequestException("Plate number already exists");
        }
        if(request.engineNumber() != null && !request.engineNumber().isBlank()) {
            if(carRepository.existsByEngineNumber(request.engineNumber()).isPresent()) {
                throw new BadRequestException("Engine number already exists");
            }
        }
        CarEntity carMapping = carMapper.toEntity(request);
        carMapping.setBrand(brand);
        carMapping.setModel(model);
        carMapping.setUser(user);
        CarEntity savedCar = carRepository.save(carMapping);
        return carMapper.toResponse(savedCar);

    }

    @Override
    public List<CarResponse> getMyCars(UUID userId) {
        return List.of();
    }

    @Override
    public CarResponse getCar(UUID userId, UUID carId) {
        return null;
    }

    @Override
    public CarResponse updateCar(UUID carId, CreateCarRequest request) {
        return null;
    }

    @Override
    public void deleteCar(UUID carId) {

    }
}
