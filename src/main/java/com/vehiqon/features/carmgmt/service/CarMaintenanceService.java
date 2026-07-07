package com.vehiqon.features.carmgmt.service;

import com.vehiqon.features.carmgmt.dto.CarMaintenanceDto;

import java.util.List;
import java.util.UUID;

public interface CarMaintenanceService {

    CarMaintenanceDto.MaintenanceResponse create(CarMaintenanceDto.CreateMaintenanceRequest request);

    List<CarMaintenanceDto.MaintenanceResponse> getMyMaintenance();

    List<CarMaintenanceDto.MaintenanceResponse> getCarMaintenance(UUID carId);

    CarMaintenanceDto.MaintenanceResponse getMaintenance(UUID id);

    CarMaintenanceDto.MaintenanceResponse update(UUID id, CarMaintenanceDto.UpdateMaintenanceRequest request);

    void cancel(UUID id);

    CarMaintenanceDto.MaintenanceResponse complete(UUID id);
}
