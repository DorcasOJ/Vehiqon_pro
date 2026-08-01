package com.vehiqon.features.carmgmt.controller;

import com.vehiqon.common.dto.response.ApiResponse;
import com.vehiqon.common.enums.EntityEnum;
import com.vehiqon.common.mapper.ApiResponseMapper;
import com.vehiqon.features.carmgmt.dto.CarDto;
import com.vehiqon.features.carmgmt.dto.response.CarDetailsResponse;
import com.vehiqon.features.carmgmt.service.CarService;
import com.vehiqon.features.insights.analytics.enums.EntityIdSource;
import com.vehiqon.features.insights.analytics.enums.EventType;
import com.vehiqon.features.insights.analytics.service.around.AnalyticsAction;
import com.vehiqon.features.insights.auditLog.enums.AuditActionType;
import com.vehiqon.features.insights.auditLog.service.around.AuditAction;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/vehicles")
@AllArgsConstructor
@RestController
@SecurityRequirement(name = "bearerAuth")
public class CarController {

    private final CarService carService;
    private final ApiResponseMapper apiResponseMapper;


    @AnalyticsAction(
            value = EventType.VEHICLE_CREATED,
            entityIdSource = EntityIdSource.NONE
    )
    @AuditAction(
            value = AuditActionType.VEHICLE_REGISTERED,
            entityType = EntityEnum.VEHICLE,
            entityIdSource = EntityIdSource.NONE
    )
    @PostMapping("/new")
    public ResponseEntity<ApiResponse<CarDto.CarResponse>> registerCar(
            @Valid @RequestBody CarDto.CreateCarRequest request
    ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(apiResponseMapper.toResponse(carService.registerCar(request))
                );
    }

//    get list of users vehicles
    @AnalyticsAction(
            value = EventType.VEHICLE_VIEWED,
            entityIdSource = EntityIdSource.NONE
    )
    @AuditAction(
            value = AuditActionType.VEHICLE_VIEWED,
            entityType = EntityEnum.VEHICLE,
            entityIdSource = EntityIdSource.NONE
    )
    @GetMapping()
    ResponseEntity<ApiResponse<List<CarDetailsResponse>>> getCarByUser(
    ){
        return ResponseEntity.ok()
                .body(apiResponseMapper.toResponse(carService.getMyCars()));
    }

//    get one vehicle of a vehicle

    @AnalyticsAction(
            value = EventType.VEHICLE_VIEWED,
            entityIdSource = EntityIdSource.PATH_VARIABLE,
            entityIdParam = "id"
    )
    @AuditAction(
            value = AuditActionType.VEHICLE_VIEWED,
            entityType = EntityEnum.VEHICLE,
            entityIdSource = EntityIdSource.PATH_VARIABLE,
            entityIdParam = "id"
    )
    @GetMapping("{id}")
    ResponseEntity<ApiResponse<CarDetailsResponse>> getCar(
            @PathVariable UUID id
    ){
        return ResponseEntity.ok()
                .body(apiResponseMapper.toResponse(carService.getCar(id)));
    }




    @PatchMapping("{carId}")
    ResponseEntity<ApiResponse<CarDto.CarResponse>> updateCar(
            @PathVariable UUID carId,
            @Valid @RequestBody CarDto.UpdateCarRequest request
    ){
        return ResponseEntity.ok().body(
                apiResponseMapper.toResponse(carService.update(carId,request ))
        );

    }

//    @DeleteMapping("/{carId}")
//    public ResponseEntity<ApiResponse<Void>> deleteCar(
//            @PathVariable UUID carId) {
//
//        carService.deleteCar(carId);
//
//        return ResponseEntity.noContent().build();
//    }

}
