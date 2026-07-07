package com.vehiqon.features.carmgmt.controller;

import com.vehiqon.common.dto.response.ApiResponse;
import com.vehiqon.features.carmgmt.dto.CarDto;
import com.vehiqon.features.carmgmt.service.CarService;
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
public class CarAdminController {

    private final CarService carService;

//    Admin APIs

    @GetMapping("users/{userId}")
    ResponseEntity<ApiResponse<List<CarDto.CarResponse>>> getAllCarByUserId(
            @PathVariable("userId") UUID userId
    ){
        return ResponseEntity.ok()
                .body(ApiResponse.<List<CarDto.CarResponse>>builder()
                        .success(true)
                        .data(carService.getCarsByUser(userId))
                        .build());
    }

    @GetMapping("users/{userId}/{carId}")
    ResponseEntity<ApiResponse<CarDto.CarResponse>> getCarByUserId(
            @PathVariable("userId") UUID userId,
            @PathVariable("carId") UUID carId
    ){
        return ResponseEntity.ok()
                .body(ApiResponse.<CarDto.CarResponse>builder()
                        .success(true)
                        .data(carService.getUserCar(userId, carId))
                        .build());
    }


    @PutMapping("users/{userId}/{carId}")
    ResponseEntity<ApiResponse<CarDto.CarResponse>> editCarByUserId(
            @PathVariable("userId") UUID userId,
            @PathVariable("carId") UUID carId,
            @RequestBody CarDto.UpdateCarRequest request
    ){
        return ResponseEntity.ok().body(ApiResponse.<CarDto.CarResponse>builder()
                // .responseCode()
                .data(carService.updateUserCar(userId, carId, request))
                .build());
    }


}
