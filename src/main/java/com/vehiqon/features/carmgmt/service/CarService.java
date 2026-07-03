package com.vehiqon.features.carmgmt.service;

import com.vehiqon.features.carmgmt.dto.request.CreateCarRequest;
import com.vehiqon.features.carmgmt.dto.response.CarResponse;
import com.vehiqon.features.onboarding.entity.UserEntity;

import java.util.List;
import java.util.UUID;

public interface CarService {
    CarResponse registerCar(UserEntity user, CreateCarRequest request);
    List<CarResponse> getMyCars(UUID userId);
    CarResponse getCar(UUID userId, UUID carId);
    CarResponse updateCar(UUID carId, CreateCarRequest request);
    void deleteCar(UUID carId);
}
