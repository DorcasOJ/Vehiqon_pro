package com.vehiqon.features.carmgmt.service;

import com.vehiqon.features.carmgmt.dto.CarDocumentDto;
import com.vehiqon.features.carmgmt.dto.CarDto;
import com.vehiqon.features.carmgmt.dto.response.CarDetailsResponse;
import com.vehiqon.features.carmgmt.enums.CarStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.UUID;

public interface CarService {
    CarDto.CarResponse registerCar(CarDto.CreateCarRequest request);
    Page<CarDetailsResponse> getMyCars(Pageable pageable);
    CarDetailsResponse getCar( UUID carId);
    CarDto.CarResponse update(UUID carId, CarDto.UpdateCarRequest request);
    void deleteCar(UUID carId);
    void restoreCar(UUID carId);
    CarDto.CarStatisticsResponse getCarStatistics();
    List<CarDto.CarEntityResponse> getCarsDeleted();

    // Admin operations
    Page<CarDetailsResponse> getAllCars(Pageable pageable);
    CarDetailsResponse getCarById(UUID carId);
    Page<CarDetailsResponse> getCarsByUser(UUID userId, Pageable pageable);
    CarDetailsResponse getUserCar(UUID userId, UUID carId);
    CarDto.CarResponse updateUserCar(UUID carId, CarDto.UpdateCarRequest request);
    void deleteMultipleCarsForUser(List<UUID> carIds);
    void deleteCarByAdmin(UUID carId);
    void deleteMultipleCarByAdmin(List<UUID> carIds);
    void restoreMultipleCars(List<UUID> carIds);
    void restoreCarByAdmin(UUID carId);

    //    both
    Page<CarDetailsResponse> searchCars(String query, UUID brandId, CarStatus status, Pageable pageable);

//    Page<CarDocumentDto.CarDocumentResponse> getDocuments(UUID carId);
}