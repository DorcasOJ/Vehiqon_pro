package com.vehiqon.features.carmgmt.service;

import com.vehiqon.common.entity.User;
import com.vehiqon.features.carmgmt.dto.CarDto;
import com.vehiqon.features.carmgmt.dto.request.CreateCarRequest;
import com.vehiqon.features.carmgmt.dto.request.UpdateCarRequest;
import com.vehiqon.features.carmgmt.entities.BrandEntity;
import com.vehiqon.features.carmgmt.entities.CarEntity;
import com.vehiqon.features.carmgmt.entities.CarModelEntity;
import com.vehiqon.features.carmgmt.exceptions.NotFoundException;
import com.vehiqon.features.carmgmt.mapper.CarMapper;
import com.vehiqon.features.carmgmt.repository.BrandRepository;
import com.vehiqon.features.carmgmt.repository.CarRepository;
import com.vehiqon.features.carmgmt.repository.ModelRepository;
import com.vehiqon.features.onboarding.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CarService {

    private final CarRepository carRepository;
    private final CarMapper carMapper;
    private final UserRepository userRepository;
    private final BrandRepository brandRepository;
    private final ModelRepository carModelRepository;


    public CarDto.CarResponse getCarsByUser(UUID userId){
        var cars = carRepository.findByUserId(userId)
                .orElse(List.of())
                .stream().map(carMapper::toCarResponseData)
                .toList();
       return new CarDto.CarResponse(
               userId, cars
       );
    }

    @Transactional
    public CarDto.CarResponse create(CreateCarRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        BrandEntity brand = brandRepository.findById(request.getBrandId())
                .orElseThrow(() -> new IllegalArgumentException("Brand not found"));

        CarModelEntity model = carModelRepository.findById(request.getModelId())
                .orElseThrow(() -> new IllegalArgumentException("Model not found"));

        CarEntity car = new CarEntity();
        car.setNickname(request.getNickname());
        car.setVin(request.getVin());
        car.setPlateNumber(request.getPlateNumber());
        car.setColor(request.getColor());
        car.setYear(request.getYear());
        car.setEngineNumber(request.getEngineNumber());
        car.setFuelType(request.getFuelType());
        car.setTransmission(request.getTransmission());
        car.setOdometer(request.getOdometer());
        car.setPurchaseDate(request.getPurchaseDate());
        car.setInsuranceExpiry(request.getInsuranceExpiry());
        car.setLicenseExpiry(request.getLicenseExpiry());
        car.setStatus(request.getStatus());

        car.setUser(user);
        car.setBrand(brand);
        car.setCarModelEntity(model);

        carRepository.save(car);

        return new CarDto.CarResponse(
                car.getId(), List.of(carMapper.toCarResponseData(car))
        );
    }



    @Transactional
    public CarDto.CarResponse update(UUID carId, UpdateCarRequest request) {

        CarEntity car = carRepository.findById(carId)
                .orElseThrow(() -> new NotFoundException("Car not found"));

        BrandEntity brand = brandRepository.findById(request.getBrandId())
                .orElseThrow(() -> new NotFoundException("Brand not found"));

        CarModelEntity model = carModelRepository.findById(request.getModelId())
                .orElseThrow(() -> new NotFoundException("Model not found"));

        car.setNickname(request.getNickname());
        car.setPlateNumber(request.getPlateNumber());
        car.setColor(request.getColor());
        car.setYear(request.getYear());
        car.setEngineNumber(request.getEngineNumber());
        car.setFuelType(request.getFuelType());
        car.setTransmission(request.getTransmission());
        car.setOdometer(request.getOdometer());
        car.setPurchaseDate(request.getPurchaseDate());
        car.setInsuranceExpiry(request.getInsuranceExpiry());
        car.setLicenseExpiry(request.getLicenseExpiry());
        car.setStatus(request.getStatus());

        car.setBrand(brand);
        car.setCarModelEntity(model);

        carRepository.save(car);

        return new CarDto.CarResponse(
                car.getId(), List.of(carMapper.toCarResponseData(car))
        );
    }
}