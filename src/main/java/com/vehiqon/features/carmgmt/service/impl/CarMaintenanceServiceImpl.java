package com.vehiqon.features.carmgmt.service.impl;

import com.vehiqon.common.exception.BadRequestException;
import com.vehiqon.common.exception.ResourceNotFoundException;
import com.vehiqon.features.carmgmt.dto.CarMaintenanceDto;
import com.vehiqon.features.carmgmt.entities.CarEntity;
import com.vehiqon.features.carmgmt.entities.MaintenanceReminderEntity;
import com.vehiqon.features.carmgmt.enums.MaintenanceStatus;
import com.vehiqon.features.carmgmt.mapper.CarMaintenanceMapper;
import com.vehiqon.features.carmgmt.repository.CarMaintenanceRepository;
import com.vehiqon.features.carmgmt.repository.CarRepository;
import com.vehiqon.features.carmgmt.service.CarMaintenanceService;
import com.vehiqon.features.onboarding.entity.UserEntity;
import com.vehiqon.features.onboarding.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarMaintenanceServiceImpl implements CarMaintenanceService {
    private final AuthService authService;
    private final CarRepository carRepository;
    private final CarMaintenanceRepository carMaintenanceRepository;
    private final CarMaintenanceMapper carMaintenanceMapper;

    @Override
    public CarMaintenanceDto.MaintenanceResponse create(CarMaintenanceDto.CreateMaintenanceRequest request) {
        UserEntity user = authService.getAuthenticatedUser();
        CarEntity car = carRepository.findByIdAndUser(request.carId(), user)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));
        if (LocalDate.parse(request.appointmentDate(), DateTimeFormatter.ofPattern("dd-MM-yyy"))
                .isBefore(LocalDate.now())) {
            throw new BadRequestException("Appointment date cannot be in the past");
        }
        if(carMaintenanceRepository.existsByCarEntityAndAppointmentDateAndAppointmentTime(
                car, LocalDate.parse(request.appointmentDate(), DateTimeFormatter.ofPattern("dd-MM-yyy")),
                LocalTime.parse(request.appointmentTime())
        )) {
            throw new BadRequestException("A maintenance appointment already exists for this time");
        }
        MaintenanceReminderEntity maintenance = carMaintenanceMapper.toEntity(request);
        maintenance.setCarEntity(car);
        maintenance.setUser(user);
        maintenance.setStatus(MaintenanceStatus.SCHEDULED);
        return carMaintenanceMapper.toResponse(carMaintenanceRepository.save(maintenance));

    }

    @Override
    @Transactional
    public List<CarMaintenanceDto.MaintenanceResponse> getMyMaintenance() {
        UserEntity user = authService.getAuthenticatedUser();
        return carMaintenanceRepository.findAllByUser(user).stream()
                .map(carMaintenanceMapper::toResponse)
                .toList();
    }

    @Override
    public List<CarMaintenanceDto.MaintenanceResponse> getCarMaintenance(UUID carId) {
        UserEntity user = authService.getAuthenticatedUser();
        CarEntity car = carRepository.findByIdAndUser(carId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));
        return carMaintenanceRepository.findAllByCarEntityAndUser(car, user)
                .stream().map(carMaintenanceMapper::toResponse)
                .toList();
    }

    @Override
    public CarMaintenanceDto.MaintenanceResponse getMaintenance(UUID id) {
        UserEntity user = authService.getAuthenticatedUser();
        MaintenanceReminderEntity maintenance = carMaintenanceRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance not found"));
        return carMaintenanceMapper.toResponse(maintenance);

    }

    @Override
    public CarMaintenanceDto.MaintenanceResponse update(UUID id, CarMaintenanceDto.UpdateMaintenanceRequest request) {
        UserEntity user = authService.getAuthenticatedUser();
        MaintenanceReminderEntity maintenance = carMaintenanceRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance not found"));
        if(request.appointmentTime() != null &&
                request.appointmentDate().isBefore(LocalDate.now())) {
            throw new BadRequestException("Appointment date cannot be in the past");
        }
        carMaintenanceMapper.updateEntity(request, maintenance);
        return carMaintenanceMapper.toResponse(
                carMaintenanceRepository.save(maintenance)
        );

    }

    @Override
    public void cancel(UUID id) {
        UserEntity user = authService.getAuthenticatedUser();
        MaintenanceReminderEntity maintenance = carMaintenanceRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance not found"));
        if(maintenance.getStatus() == MaintenanceStatus.COMPLETED) {
            throw new BadRequestException("Completed maintenance cannot be cancelled");
        }
        maintenance.setStatus(MaintenanceStatus.CANCELLED);
        carMaintenanceRepository.save(maintenance);

    }

    @Override
    public CarMaintenanceDto.MaintenanceResponse complete(UUID id) {
        UserEntity user = authService.getAuthenticatedUser();
        MaintenanceReminderEntity maintenance = carMaintenanceRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance not found"));
        maintenance.setStatus(MaintenanceStatus.COMPLETED);

        return carMaintenanceMapper.toResponse(carMaintenanceRepository.save(maintenance));
    }
}
