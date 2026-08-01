package com.vehiqon.features.carmgmt.controller;

import com.vehiqon.common.dto.response.ApiResponse;
import com.vehiqon.common.mapper.ApiResponseMapper;
import com.vehiqon.features.carmgmt.dto.CarDto;
import com.vehiqon.features.carmgmt.dto.response.CarDetailsResponse;
import com.vehiqon.features.carmgmt.enums.CarStatus;
import com.vehiqon.features.carmgmt.service.CarService;
import com.vehiqon.features.insights.analytics.enums.EntityIdSource;
import com.vehiqon.features.insights.analytics.enums.EventType;
import com.vehiqon.features.insights.analytics.service.around.AnalyticsAction;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/admin/vehicles")
@AllArgsConstructor
@RestController
@SecurityRequirement(name = "bearerAuth")
public class CarAdminController {

    private final CarService carService;
    private final ApiResponseMapper apiResponseMapper;


//    Admin APIs

    @GetMapping("users/{userId}")
    ResponseEntity<ApiResponse<List<CarDetailsResponse>>> getAllCarByUserId(
            @PathVariable("userId") UUID userId
    ){
        return ResponseEntity.ok()
                .body(apiResponseMapper.toResponse(carService.getCarsByUser(userId))
                );

    }

    @GetMapping("users/{userId}/{carId}")
    ResponseEntity<ApiResponse<CarDetailsResponse>> getCarByUserId(
            @PathVariable("userId") UUID userId,
            @PathVariable("carId") UUID carId
    ){
        return ResponseEntity.ok()
                .body(apiResponseMapper.toResponse(carService.getUserCar(userId, carId))
                );
    }


    @AnalyticsAction(
            value = EventType.VEHICLE_VIEWED,
            entityIdSource = EntityIdSource.PATH_VARIABLE,
            entityIdParam = "id"
    )
    @GetMapping("/search")
    ResponseEntity<ApiResponse<Page<CarDetailsResponse>>> searchCar(
            @RequestParam String query,
            @RequestParam(required = false) UUID brandId,
            @RequestParam(required = false) CarStatus status,
            @PageableDefault(page = 0, size = 15, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        Page<CarDetailsResponse> carDetailsResponses = carService.searchCars(query, brandId, status, pageable);
        return ResponseEntity.ok()
                .body(apiResponseMapper.toResponse(carDetailsResponses));
    }

    @PatchMapping("users/{userId}/{carId}")
    ResponseEntity<ApiResponse<CarDto.CarResponse>> editCarByUserId(
            @PathVariable("userId") UUID userId,
            @PathVariable("carId") UUID carId,
            @Valid @RequestBody CarDto.UpdateCarRequest request
    ){
        return ResponseEntity.ok().body(apiResponseMapper.toResponse(carService.updateUserCar(userId, carId, request))
        );

    }


}
