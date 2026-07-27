package com.vehiqon.features.carmgmt.controller;

import com.vehiqon.common.dto.response.ApiResponse;
import com.vehiqon.common.mapper.ApiResponseMapper;
import com.vehiqon.features.carmgmt.dto.MaintenanceReminderDto;
import com.vehiqon.features.carmgmt.dto.response.MaintenanceReminderResponse;
import com.vehiqon.features.carmgmt.service.CarMaintenanceService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/maintenance")
@AllArgsConstructor
@RestController
@SecurityRequirement(name = "bearerAuth")
public class CarMaintenanceController {
//    private final CarMaintenanceService maintenanceService;
//    private final ApiResponseMapper apiResponseMapper;
//
//    @PostMapping
//    public ResponseEntity<ApiResponse<MaintenanceReminderDto.MaintenanceResponse>> create(
//            @Valid @RequestBody MaintenanceReminderDto.CreateMaintenanceRequest request
//            ) {
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(apiResponseMapper.toResponse(
//                        maintenanceService.create(request)
//                ));
//
//    }
//
//    @GetMapping
//    public ResponseEntity<ApiResponse<List<MaintenanceReminderResponse>>> getMyMaintenance() {
//        return ResponseEntity.ok(
//                apiResponseMapper.toResponse(
//                        maintenanceService.getMyMaintenance()
//                )
//        );
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<ApiResponse<MaintenanceReminderResponse>> getMaintenance(
//            @PathVariable UUID id
//            ) {
//        return ResponseEntity.ok(
//                apiResponseMapper.toResponse(
//                        maintenanceService.getMaintenance(id)
//                )
//        );
//
//    }
//
//    @GetMapping("/car/{carId}")
//    public ResponseEntity<ApiResponse<List<MaintenanceReminderResponse>>> getCarMaintenance(
//            @PathVariable UUID carId
//    ) {
//        return ResponseEntity.ok(
//                apiResponseMapper.toResponse(
//                        maintenanceService.getCarMaintenance(carId)
//                )
//        );
//    }
//
//    @PatchMapping("/{id}")
//    public ResponseEntity<ApiResponse<MaintenanceReminderDto.MaintenanceResponse>> update(
//            @PathVariable UUID id,
//            @Valid @RequestBody MaintenanceReminderDto.UpdateMaintenanceRequest request
//            ) {
//        return ResponseEntity.ok(
//                apiResponseMapper.toResponse(maintenanceService.update(id, request))
//        );
//    }
//
//    @PatchMapping("/{id}/cancel")
//    public ResponseEntity<ApiResponse<Object>> cancel(
//            @PathVariable UUID id
//    ) {
//        maintenanceService.cancel(id);
//        return ResponseEntity.ok(
//                apiResponseMapper.toResponse("Maintenance cancelled successfully")
//        );
//    }
//
//    @PatchMapping("/{id}/complete")
//    public ResponseEntity<ApiResponse<MaintenanceReminderDto.MaintenanceResponse>> complete(
//            @PathVariable UUID id
//    ) {
//        return ResponseEntity.ok(
//                apiResponseMapper.toResponse(maintenanceService.complete(id))
//        );
//
//    }
}
