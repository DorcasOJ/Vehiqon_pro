package com.vehiqon.features.carmgmt.service.impl;

import com.vehiqon.features.carmgmt.mapper.CarMaintenanceMapper;
import com.vehiqon.features.carmgmt.repository.CarMaintenanceRepository;
import com.vehiqon.features.carmgmt.repository.CarRepository;
import com.vehiqon.features.onboarding.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarMaintenanceServiceImpl  {
    private final AuthService authService;
    private final CarRepository carRepository;
    private final CarMaintenanceRepository carMaintenanceRepository;
    private final CarMaintenanceMapper carMaintenanceMapper;

//    @Override
//    public CarMaintenanceDto.MaintenanceResponse create(CarMaintenanceDto.CreateMaintenanceRequest request) {
//        CustomerUserDetails authenticatedUser = authService.getAuthenticatedUser();
//
//        CarEntity car = carRepository.findByIdAndUserId(request.carId(), authenticatedUser.user().getId())
//                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));
//        if (request.appointmentDate()
//                .isBefore(LocalDate.now())) {
//            throw new BadRequestException("Appointment date cannot be in the past");
//        }
//        if(carMaintenanceRepository.existsByCarIdAndAppointmentDateAndAppointmentTime(
//                car.getId(), request.appointmentDate(),
//                request.appointmentTime())) {
//            throw new BadRequestException("A maintenance appointment already exists for this time");
//        }
//        MaintenanceReminderEntity maintenance = carMaintenanceMapper.toEntity(request);
//        maintenance.setCarId(car.getId());
////        maintenance.setUserId(user.getId());
//        maintenance.setStatus(MaintenanceStatus.valueOf(MaintenanceStatus.SCHEDULED.name()));
//        maintenance.setNotificationDate(
//                request.notificationDate() != null
//                        ? request.notificationDate()
//                        : maintenance.getDueDate().minusDays(3)
//        );
//        maintenance.setNotificationSent(false);
//        return carMaintenanceMapper.toResponse(carMaintenanceRepository.save(maintenance));
//
//    }
//
//    @Override
//    @Transactional
//    public List<MaintenanceReminderResponse> getMyMaintenance() {
//        CustomerUserDetails authenticatedUser = authService.getAuthenticatedUser();
//
//
//       return carMaintenanceRepository.findAllMaintenanceReminderByUserId(authenticatedUser.user().getId()).orElseThrow(() -> new ResourceNotFoundException("Maintenance Reminder not found for user"));
//    }
//
//    @Override
//    public List<MaintenanceReminderResponse> getCarMaintenance(UUID carId) {
//        CustomerUserDetails authenticatedUser = authService.getAuthenticatedUser();
//
//
//        CarEntity car = carRepository.findByIdAndUserId(carId, authenticatedUser.user().getId())
//                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));
//        return carMaintenanceRepository.findMaintenanceReminderByCarId(car.getId())
//                .orElseThrow(() -> new ResourceNotFoundException("Maintenance not found"));
//
//
//    }
//
//    @Override
//    public MaintenanceReminderResponse getMaintenance(UUID id) {
//        CustomerUserDetails authenticatedUser = authService.getAuthenticatedUser();
//        return carMaintenanceRepository.findMaintenanceReminderByUserId(authenticatedUser.user().getId(), id)
//                .orElseThrow(() -> new ResourceNotFoundException("Maintenance not found"));
//
//
//    }
//
//    @Override
//    public CarMaintenanceDto.MaintenanceResponse update(UUID id, CarMaintenanceDto.UpdateMaintenanceRequest request) {
//        CustomerUserDetails authenticatedUser = authService.getAuthenticatedUser();
//
//
//        MaintenanceReminderEntity maintenance = carMaintenanceRepository.findByIdAndUserId(id, authenticatedUser.user().getId())
//                .orElseThrow(() -> new ResourceNotFoundException("Maintenance not found"));
//        if(request.appointmentTime() != null &&
//                request.appointmentDate().isBefore(LocalDate.now())) {
//            throw new BadRequestException("Appointment date cannot be in the past");
//        }
//        carMaintenanceMapper.updateEntity(request, maintenance);
//        return carMaintenanceMapper.toResponse(
//                carMaintenanceRepository.save(maintenance)
//        );
//
//    }
//
//    @Override
//    public void cancel(UUID id) {
//        CustomerUserDetails authenticatedUser = authService.getAuthenticatedUser();
//
//
//        MaintenanceReminderEntity maintenance = carMaintenanceRepository.findByIdAndUserId(id, authenticatedUser.user().getId())
//                .orElseThrow(() -> new ResourceNotFoundException("Maintenance not found"));
//        if(maintenance.getStatus() == MaintenanceStatus.COMPLETED) {
//            throw new BadRequestException("Completed maintenance cannot be cancelled");
//        }
//        maintenance.setStatus(MaintenanceStatus.CANCELLED);
//        carMaintenanceRepository.save(maintenance);
//
//    }
//
//    @Override
//    public CarMaintenanceDto.MaintenanceResponse complete(UUID id) {
//        CustomerUserDetails authenticatedUser = authService.getAuthenticatedUser();
//
//
//        MaintenanceReminderEntity maintenance = carMaintenanceRepository.findByIdAndUserId(id, authenticatedUser.user().getId())
//                .orElseThrow(() -> new ResourceNotFoundException("Maintenance not found"));
//        maintenance.setStatus(MaintenanceStatus.COMPLETED);
//
//        return carMaintenanceMapper.toResponse(carMaintenanceRepository.save(maintenance));
//    }
}
