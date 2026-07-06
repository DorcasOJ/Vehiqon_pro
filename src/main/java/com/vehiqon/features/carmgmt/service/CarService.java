package com.vehiqon.features.carmgmt.service;

import com.vehiqon.features.carmgmt.dto.CarDto;
import com.vehiqon.features.carmgmt.dto.request.CreateCarRequest;


import java.util.List;
import java.util.UUID;

public interface CarService {
    CarDto.CarResponse registerCar(CarDto.CreateCarRequest request);
    List<CarDto.CarResponse> getMyCars();
    CarDto.CarResponse getCar( UUID carId);
    CarDto.CarResponse update(UUID carId, CarDto.UpdateCarRequest request);
    void deleteCar(UUID carId);

    // Admin operations
    List<CarDto.CarResponse> getCarsByUser(UUID userId);
    CarDto.CarResponse getUserCar(UUID userId, UUID carId);
    CarDto.CarResponse updateUserCar(UUID userId, UUID carId, CarDto.UpdateCarRequest request);
}