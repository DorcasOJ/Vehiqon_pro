package com.vehiqon.features.carmgmt.controller;

import com.vehiqon.common.dto.response.ApiResponse;
import com.vehiqon.common.enums.EntityEnum;
import com.vehiqon.common.dto.mapper.ApiResponseMapper;
import com.vehiqon.features.carmgmt.dto.CarDto;
import com.vehiqon.features.carmgmt.dto.response.CarDetailsResponse;
import com.vehiqon.features.carmgmt.service.CarService;
import com.vehiqon.features.insights.analytics.enums.EntityIdSource;
import com.vehiqon.features.insights.analytics.enums.EventType;
import com.vehiqon.features.insights.analytics.service.around.AnalyticsAction;
import com.vehiqon.features.insights.auditLog.enums.AuditActionType;
import com.vehiqon.features.insights.auditLog.service.around.AuditAction;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    /**
     *
     * @return list of users vehicles
     */
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
    ResponseEntity<ApiResponse<Page<CarDetailsResponse>>> getCarByUser(
            @Parameter(hidden = true)
            @PageableDefault(page = 0, size = 15, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable

    ){
        return ResponseEntity.ok()
                .body(apiResponseMapper.toResponse(carService.getMyCars(extractPageable(pageable))));
    }

    /**
     *
     * @return list of users deleted vehicles
     */
    @AnalyticsAction(
            value = EventType.VEHICLE_VIEWED,
            entityIdSource = EntityIdSource.NONE
    )
    @AuditAction(
            value = AuditActionType.VEHICLE_VIEWED,
            entityType = EntityEnum.VEHICLE,
            entityIdSource = EntityIdSource.NONE
    )
    @GetMapping("/deleted")
    ResponseEntity<ApiResponse<List<CarDto.CarEntityResponse>>> getDeletedCarByUser(){
        return ResponseEntity.ok()
                .body(apiResponseMapper.toResponse(carService.getCarsDeleted()));
    }


    /**
     *
     * @return one of users vehicles
     */
    @AnalyticsAction(
            value = EventType.VEHICLE_VIEWED,
            entityIdSource = EntityIdSource.PATH_VARIABLE,
            entityIdParam = "carId"
    )
    @AuditAction(
            value = AuditActionType.VEHICLE_VIEWED,
            entityType = EntityEnum.VEHICLE,
            entityIdSource = EntityIdSource.PATH_VARIABLE,
            entityIdParam = "carId"
    )
    @GetMapping("/{carId}")
    ResponseEntity<ApiResponse<CarDetailsResponse>> getCar(
            @PathVariable UUID carId
    ){
        return ResponseEntity.ok()
                .body(apiResponseMapper.toResponse(carService.getCar(carId)));
    }


    /**
     *
     * @return total of users vehicles statistics; total, active and  deleted
     */
    @AnalyticsAction(
            value = EventType.GET_VEHICLE_STATISTICS,
            entityIdSource = EntityIdSource.NONE
    )
    @AuditAction(
            value = AuditActionType.GET_VEHICLE_STATISTICS,
            entityType = EntityEnum.VEHICLE,
            entityIdSource = EntityIdSource.NONE
    )
    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<CarDto.CarStatisticsResponse>> getCarStatistics() {
        return ResponseEntity.ok(
                apiResponseMapper.toResponse( carService.getCarStatistics())
        );
    }


    /**
     *
     * @return a newly registered car
     */
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


    /**
     *
     * @return an updated vehicle
     */
    @AnalyticsAction(
            value = EventType.VEHICLE_UPDATED,
            entityIdSource = EntityIdSource.PATH_VARIABLE,
            entityIdParam = "carId"
    )
    @AuditAction(
            value = AuditActionType.VEHICLE_UPDATED,
            entityType = EntityEnum.VEHICLE,
            entityIdSource = EntityIdSource.PATH_VARIABLE,
            entityIdParam = "carId"
    )
    @PatchMapping("{carId}")
    public ResponseEntity<ApiResponse<CarDto.CarResponse>> updateCar(
            @PathVariable UUID carId,
            @Valid @RequestBody CarDto.UpdateCarRequest request
    ){
        return ResponseEntity.ok().body(
                apiResponseMapper.toResponse(carService.update(carId,request ))
        );
    }


    /**
     *
     * @return a string upon successful delete of vehicle
     */
    @AnalyticsAction(
            value = EventType.VEHICLE_DELETED,
            entityIdSource = EntityIdSource.PATH_VARIABLE,
            entityIdParam = "carId"
    )
    @AuditAction(
            value = AuditActionType.VEHICLE_DELETED,
            entityType = EntityEnum.VEHICLE,
            entityIdSource = EntityIdSource.PATH_VARIABLE,
            entityIdParam = "carId"
    )
    @DeleteMapping("/{carId}")
    public ResponseEntity<ApiResponse<String>> deleteCar(
            @PathVariable UUID carId) {
        carService.deleteCar(carId);
        return ResponseEntity.ok()
                .body(apiResponseMapper.toResponse("Delete Successfully"));

    }


    /**
     *
     * @return a string upon successful bulk delete of vehicle
     */
    @AnalyticsAction(
            value = EventType.VEHICLE_UPDATED,
            entityIdSource = EntityIdSource.NONE
    )
    @AuditAction(
            value = AuditActionType.VEHICLE_UPDATED,
            entityType = EntityEnum.VEHICLE,
            entityIdSource = EntityIdSource.NONE
    )
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<String>> deleteAllUserCar(
            @RequestBody List<UUID> carIds) {
        carService.deleteMultipleCarsForUser(carIds);
        return ResponseEntity.ok()
                .body(apiResponseMapper.toResponse("Delete Successfully"));

    }

    /**
     *
     * @return a string upon successful restore of vehicle
     */
    @AnalyticsAction(
            value = EventType.VEHICLE_RESTORED,
            entityIdSource = EntityIdSource.PATH_VARIABLE,
            entityIdParam = "carId"
    )
    @AuditAction(
            value = AuditActionType.VEHICLE_RESTORED,
            entityType = EntityEnum.VEHICLE,
            entityIdSource = EntityIdSource.PATH_VARIABLE,
            entityIdParam = "carId"
    )
    @PostMapping("/{carId}/restore")
    public ResponseEntity<ApiResponse<String>> restoreCar(
            @PathVariable UUID carId) {
        carService.restoreCar(carId);
        return ResponseEntity.ok()
                .body(apiResponseMapper.toResponse("Restored Successfully"));

    }

    private Pageable extractPageable(Pageable pageable) {
        boolean hasInvalidSort = pageable.getSort().stream()
                .anyMatch(order -> order.getProperty().equalsIgnoreCase("string"));
        if (hasInvalidSort) {
            return PageRequest.of(
                    pageable.getPageNumber(), pageable.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "createdAt")
            );
        }
        return pageable;

    }


}
