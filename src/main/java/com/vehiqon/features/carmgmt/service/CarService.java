package com.vehiqon.features.carmgmt.service;

import com.vehiqon.features.carmgmt.dto.CarDto;
import com.vehiqon.features.carmgmt.dto.request.CreateCarRequest;
import com.vehiqon.features.carmgmt.dto.response.CarDetailsResponse;


import java.util.List;
import java.util.UUID;

public interface CarService {
    CarDto.CarResponse registerCar(CarDto.CreateCarRequest request);
    List<CarDetailsResponse> getMyCars();
    CarDetailsResponse getCar( UUID carId);
    CarDto.CarResponse update(UUID carId, CarDto.UpdateCarRequest request);
//    void deleteCar(UUID carId);

    // Admin operations
    List<CarDetailsResponse> getCarsByUser(UUID userId);
    CarDetailsResponse getUserCar(UUID userId, UUID carId);
    CarDto.CarResponse updateUserCar(UUID userId, UUID carId, CarDto.UpdateCarRequest request);

}