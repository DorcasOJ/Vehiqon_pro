package com.vehiqon.features.carmgmt.service.impl;

import com.vehiqon.common.exception.BadRequestException;
import com.vehiqon.common.exception.ResourceNotFoundException;
import com.vehiqon.features.carmgmt.dto.CarDto;
import com.vehiqon.features.carmgmt.dto.request.CreateCarRequest;
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
import com.vehiqon.features.onboarding.service.AuthService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service

public class CarServiceImpl implements CarService {
    private final AuthService authService;
    private final CarRepository carRepository;
    private final CarBrandRepository brandRepository;
    private final CarModelRepository modelRepository;
    private final UserRepository userRepository;
    private final CarMapper carMapper;


    public CarServiceImpl(AuthService authService, CarRepository carRepository, CarBrandRepository brandRepository, CarModelRepository modelRepository, UserRepository userRepository, CarMapper carMapper) {
        this.authService = authService;
        this.carRepository = carRepository;
        this.brandRepository = brandRepository;
        this.modelRepository = modelRepository;
        this.userRepository = userRepository;
        this.carMapper = carMapper;
    }

    @Override
    public CarDto.CarResponse registerCar(CarDto.CreateCarRequest request) {
        UserEntity user = authService.getAuthenticatedUser();
//        System.out.printf("Auth User {}", user.toString());
//        System.out.println("gotten userrrrrrrrrrrrrrrrrrrrr");
//        UserEntity user = userRepository.findById(request.userId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        BrandEntity brand = brandRepository.findById(request.brandId())
                .orElseThrow(() -> new ResourceNotFoundException("Car brand not found"));
        CarModelEntity model = modelRepository.findById(request.modelId())
                .orElseThrow(() -> new ResourceNotFoundException("Model not found"));
        if(!model.getBrand().getId().equals(brand.getId())) {
            throw new BadRequestException("Selected model does not belong to selected brand");
        }
//        System.out.println("verified model and brandddddddddddddddd");

      validateUniqueFields(request);

        CarEntity carMapping = carMapper.toEntity(request);
        carMapping.setUser(user);
        carMapping.setBrand(brand);
        carMapping.setModel(model);
        return carMapper.toResponse(carRepository.save(carMapping));

    }


    @Override
    public List<CarDto.CarResponse> getMyCars() {
        return List.of();
    }

    @Override
    public CarDto.CarResponse getCar(UUID carId) {
        return null;
    }

    @Override
    public CarDto.CarResponse update(UUID carId, CarDto.UpdateCarRequest request) {
        return null;
    }

    @Override
    public void deleteCar(UUID carId) {

    }

    @Override
    public List<CarDto.CarResponse> getCarsByUser(UUID userId) {
        return List.of();
    }

    @Override
    public CarDto.CarResponse getUserCar(UUID userId, UUID carId) {
        return null;
    }

    @Override
    public CarDto.CarResponse updateUserCar(UUID userId, UUID carId, CarDto.UpdateCarRequest request) {
        return null;
    }


    private void validateUniqueFields(CarDto.CreateCarRequest request) {

        if (carRepository.existsByVin(request.vin()).isPresent()) {
            throw new BadRequestException("VIN already exists");
        }

        if (carRepository.existsByPlateNumber(request.plateNumber()).isPresent()) {
            throw new BadRequestException("Plate number already exists");
        }

        if (request.engineNumber() != null
                && !request.engineNumber().isBlank()
                && carRepository.existsByEngineNumber(request.engineNumber()).isPresent()) {
            throw new BadRequestException("Engine number already exists");
        }
    }
}
