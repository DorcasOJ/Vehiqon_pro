package com.vehiqon.features.carmgmt.controller;

import com.vehiqon.common.dto.response.ApiResponse;
import com.vehiqon.common.mapper.ApiResponseMapper;
import com.vehiqon.features.carmgmt.dto.CarDto;
import com.vehiqon.features.carmgmt.dto.response.CarDetailsResponse;
import com.vehiqon.features.carmgmt.service.CarService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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
