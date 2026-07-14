package com.vehiqon.features.carmgmt.service;

import com.vehiqon.features.carmgmt.dto.CarMaintenanceDto;
import com.vehiqon.features.carmgmt.dto.response.MaintenanceReminderResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarMaintenanceService {

    CarMaintenanceDto.MaintenanceResponse create(CarMaintenanceDto.CreateMaintenanceRequest request);
    List<MaintenanceReminderResponse> getMyMaintenance();

    List<MaintenanceReminderResponse> getCarMaintenance(UUID carId);

    MaintenanceReminderResponse getMaintenance(UUID id);

    CarMaintenanceDto.MaintenanceResponse update(UUID id, CarMaintenanceDto.UpdateMaintenanceRequest request);

    void cancel(UUID id);

    CarMaintenanceDto.MaintenanceResponse complete(UUID id);
}
