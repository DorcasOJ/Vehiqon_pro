package com.vehiqon.features.carmgmt.service.impl;

import com.vehiqon.common.exception.BadRequestException;
import com.vehiqon.common.exception.ResourceNotFoundException;
import com.vehiqon.features.carmgmt.dto.CarDto;
import com.vehiqon.features.carmgmt.dto.response.CarDetailsResponse;
import com.vehiqon.features.carmgmt.entities.BrandEntity;
import com.vehiqon.features.carmgmt.entities.CarEntity;
import com.vehiqon.features.carmgmt.entities.CarModelEntity;
import com.vehiqon.features.carmgmt.enums.CarStatus;
import com.vehiqon.features.carmgmt.mapper.CarBrandModelMapper;
import com.vehiqon.features.carmgmt.mapper.CarMapper;
import com.vehiqon.features.carmgmt.mapper.CarResponseMapper;
import com.vehiqon.features.carmgmt.repository.CarBrandRepository;
import com.vehiqon.features.carmgmt.repository.CarModelRepository;
import com.vehiqon.features.carmgmt.repository.CarRepository;
import com.vehiqon.features.carmgmt.service.CarService;
import com.vehiqon.features.onboarding.entity.UserEntity;
import com.vehiqon.features.onboarding.repository.UserRepository;
import com.vehiqon.features.onboarding.service.AuthService;
import com.vehiqon.security.model.CustomerUserDetails;
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
    private final CarResponseMapper carResponseMapper;


    public CarServiceImpl(AuthService authService, CarRepository carRepository, CarBrandRepository brandRepository, CarModelRepository modelRepository, UserRepository userRepository, CarMapper carMapper, CarBrandModelMapper carBrandModelMapper, CarResponseMapper carResponseMapper) {
        this.authService = authService;
        this.carRepository = carRepository;
        this.brandRepository = brandRepository;
        this.modelRepository = modelRepository;
        this.userRepository = userRepository;
        this.carMapper = carMapper;
        this.carResponseMapper = carResponseMapper;
    }

    @Override
    public CarDto.CarResponse registerCar(CarDto.CreateCarRequest request) {
        CustomerUserDetails authenticatedUser = authService.getAuthenticatedUser();
        BrandEntity brand = brandRepository.findById(request.carBrandId())
                .orElseThrow(() -> new ResourceNotFoundException("Car brand not found"));
        CarModelEntity model = modelRepository.findById(request.carModelId())
                .orElseThrow(() -> new ResourceNotFoundException("Model not found"));
        if(!model.getCarBrandId().equals(brand.getId())) {
            throw new BadRequestException("Selected model does not belong to selected brand");
        }
        validateUniqueFields(request);

        CarEntity carMapping = carMapper.toEntity(request);
        carMapping.setUserId(authenticatedUser.user().getId());
        carMapping.setCarBrandId(brand.getId());
        carMapping.setCarModelId(model.getId());
        carMapping.setStatus(CarStatus.ACTIVE);
        return carResponseMapper.toResponse(carMapper.toResponse(carRepository.save(carMapping)));
    }


    @Override
    public List<CarDetailsResponse> getMyCars() {
        CustomerUserDetails authenticatedUser = authService.getAuthenticatedUser();

        return carRepository.findCarsDetailsByUserId(authenticatedUser.user().getId()).orElseThrow(() -> new ResourceNotFoundException("Cars not found"));
//        return carRepository.findAllByUserId(user.getId())
//                .stream()
//                .map(carMapper::toResponse)
//                .map(carResponseMapper::toResponse)
//                .flatMap(entity -> carMapper.toListResponse(entity).stream())
//                .toList();
    }

    @Override
    public CarDetailsResponse getCar(UUID carId) {
        CustomerUserDetails authenticatedUser = authService.getAuthenticatedUser();

        return carRepository.findCarDetails(carId,authenticatedUser.user().
getId())
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));

    }

    @Override
    public CarDto.CarResponse update(UUID carId, CarDto.UpdateCarRequest request) {
        CustomerUserDetails authenticatedUser = authService.getAuthenticatedUser();

        CarEntity car = carRepository.findByIdAndUserId(carId, authenticatedUser.user().
getId())
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));

        validateUpdateFields(request, car);
        carMapper.updateEntity(request, car);

        return carResponseMapper.toResponse(carMapper.toResponse(carRepository.save(car))
        );

    }

//    @Override
//    public void deleteCar(UUID carId) {
//        CustomerUserDetails authenticatedUser = authService.getAuthenticatedUser();
//
//        CarEntity car = carRepository.findByIdAndUser(carId, user)
//                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));
//
//        carRepository.delete(car);
//    }


    @Override
    public List<CarDetailsResponse> getCarsByUser(UUID userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return carRepository.findCarsDetailsByUserId(user.getId()).orElseThrow(() -> new ResourceNotFoundException("Cars not found"));
    }

    @Override
    public CarDetailsResponse getUserCar(UUID userId, UUID carId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return carRepository.findCarDetails(carId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));

    }

    @Override
    public CarDto.CarResponse updateUserCar(UUID userId, UUID carId, CarDto.UpdateCarRequest request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        CarEntity car = carRepository.findByIdAndUserId(carId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));
        validateUpdateFields(request, car);

        carMapper.updateEntity(request, car);

        CarEntity savedCar = carRepository.save(car);
        return carResponseMapper.toResponse(carMapper.toResponse(savedCar));

    }

    private void validateUpdateFields(CarDto.CarRequest request, CarEntity car ){
        BrandEntity brand = brandRepository.findById(
                        request.carBrandId() != null ? request.carBrandId() : car.getCarBrandId())
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found"));

        CarModelEntity model = modelRepository.findById(
                        request.carModelId() != null ? request.carModelId() : car.getCarModelId() )
                .orElseThrow(() -> new ResourceNotFoundException("Model not found"));

        if (!model.getCarBrandId().equals(brand.getId())) {
            throw new BadRequestException("Selected model does not belong to the selected brand");
        }

        validateUniqueFields(request);
    }

    private void validateUniqueFields(CarDto.CarRequest request) {
        if (request.vin() != null && carRepository.existsByVin(request.vin())) {
            throw new BadRequestException("VIN already exists");
        }

        if (request.plateNumber() != null && carRepository.existsByPlateNumber(request.plateNumber())) {
            throw new BadRequestException("Plate number already exists");
        }

        if (request.engineNumber() != null && request.engineNumber() != null
                && !request.engineNumber().isBlank()
                && carRepository.existsByEngineNumber(request.engineNumber())) {
            throw new BadRequestException("Engine number already exists");
        }
    }
}
