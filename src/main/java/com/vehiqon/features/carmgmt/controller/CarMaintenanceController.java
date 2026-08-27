package com.vehiqon.features.carmgmt.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
//        return ResponseEntity.documentStatus(HttpStatus.CREATED)
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
