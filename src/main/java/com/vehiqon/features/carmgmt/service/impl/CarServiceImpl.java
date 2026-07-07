package com.vehiqon.features.carmgmt.service.impl;

import com.vehiqon.common.exception.BadRequestException;
import com.vehiqon.common.exception.ResourceNotFoundException;
import com.vehiqon.features.carmgmt.dto.CarDto;
import com.vehiqon.features.carmgmt.dto.request.CreateCarRequest;
import com.vehiqon.features.carmgmt.entities.BrandEntity;
import com.vehiqon.features.carmgmt.entities.CarEntity;
import com.vehiqon.features.carmgmt.entities.CarModelEntity;
import com.vehiqon.features.carmgmt.enums.TransmissionEnum;
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
        BrandEntity brand = brandRepository.findById(request.brandId())
                .orElseThrow(() -> new ResourceNotFoundException("Car brand not found"));
        CarModelEntity model = modelRepository.findById(request.modelId())
                .orElseThrow(() -> new ResourceNotFoundException("Model not found"));
        if(!model.getBrand().getId().equals(brand.getId())) {
            throw new BadRequestException("Selected model does not belong to selected brand");
        }

      validateUniqueFields(request);

        CarEntity carMapping = carMapper.toEntity(request);
        carMapping.setUser(user);
        carMapping.setBrand(brand);
        carMapping.setModel(model);
        return carMapper.toResponse(carRepository.save(carMapping));

    }


    @Override
    public List<CarDto.CarResponse> getMyCars() {
        UserEntity user = authService.getAuthenticatedUser();

        return carRepository.findAllByUser(user)
                .stream()
                .flatMap(entity -> carMapper.toListResponse(entity).stream())
                .toList();
    }

    @Override
    public CarDto.CarResponse getCar(UUID carId) {
        UserEntity user = authService.getAuthenticatedUser();

        CarEntity car = carRepository.findByIdAndUser(carId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));

        return carMapper.toResponse(car);
    }

    @Override
    public CarDto.CarResponse update(UUID carId, CarDto.CreateCarRequest request) {
        UserEntity user = authService.getAuthenticatedUser();

        CarEntity car = carRepository.findByIdAndUser(carId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));

        BrandEntity brand = brandRepository.findById(request.brandId())
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found"));

        CarModelEntity model = modelRepository.findById(request.modelId())
                .orElseThrow(() -> new ResourceNotFoundException("Model not found"));

        if (!model.getBrand().getId().equals(brand.getId())) {
            throw new BadRequestException("Selected model does not belong to the selected brand");
        }

        car.setBrand(brand);
        car.setModel(model);
        car.setPlateNumber(request.plateNumber());
        car.setVin(request.vin());
        car.setVin(request.color());
        car.setNickname(request.nickname());
        car.setVin(request.year().toString());
        car.setStatus(request.status());
        car.setVin(request.transmission().name().toUpperCase());
        car.setVin(request.fuelType().name());
        car.setVin(request.purchaseDate());
        car.setVin(request.licenseExpiry());
        car.setVin( request.odometer().toString());
        car.setEngineNumber(request.engineNumber());

        return carMapper.toResponse(carRepository.save(car));

    }

//    @Override
//    public void deleteCar(UUID carId) {
//        UserEntity user = authService.getAuthenticatedUser();
//
//        CarEntity car = carRepository.findByIdAndUser(carId, user)
//                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));
//
//        carRepository.delete(car);
//    }


    @Override
    public List<CarDto.CarResponse> getCarsByUser(UUID userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return carRepository.findAllByUser(user)
                .stream()
                .flatMap(entity -> carMapper.toListResponse(entity).stream())
                .toList();
    }

    @Override
    public CarDto.CarResponse getUserCar(UUID userId, UUID carId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        CarEntity car = carRepository.findByIdAndUser(carId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));

        return carMapper.toResponse(car);
    }

    @Override
    public CarDto.CarResponse updateUserCar(UUID userId, UUID carId, CarDto.CreateCarRequest request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        CarEntity car = carRepository.findByIdAndUser(carId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));

        BrandEntity brand = brandRepository.findById(request.brandId())
                .orElseThrow(() -> new ResourceNotFoundException("Car brand not found"));

        CarModelEntity model = modelRepository.findById(request.modelId())
                .orElseThrow(() -> new ResourceNotFoundException("Car model not found"));

        if (!model.getBrand().getId().equals(brand.getId())) {
            throw new BadRequestException("Selected model does not belong to the selected brand");
        }
        validateUniqueFields(request);

        car.setBrand(brand);
        car.setModel(model);
        car.setPlateNumber(request.plateNumber());
        car.setVin(request.vin());
        car.setVin(request.color());
        car.setNickname(request.nickname());
        car.setVin(request.year().toString());
        car.setStatus(request.status());
        car.setVin(request.transmission().name().toUpperCase());
        car.setVin(request.fuelType().name());
        car.setVin(request.purchaseDate());
        car.setVin(request.licenseExpiry());
        car.setVin( request.odometer().toString());
        car.setEngineNumber(request.engineNumber());

        return carMapper.toResponse(carRepository.save(car));

    }


    private void validateUniqueFields(CarDto.CreateCarRequest request) {

        if (carRepository.existsByVin(request.vin())) {
            throw new BadRequestException("VIN already exists");
        }

        if (carRepository.existsByPlateNumber(request.plateNumber())) {
            throw new BadRequestException("Plate number already exists");
        }

        if (request.engineNumber() != null
                && !request.engineNumber().isBlank()
                && carRepository.existsByEngineNumber(request.engineNumber())) {
            throw new BadRequestException("Engine number already exists");
        }
    }
}
