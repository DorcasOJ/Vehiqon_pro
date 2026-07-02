package com.vehiqon.features.carmgmt.controller;

import com.vehiqon.common.dto.response.ApiResponse;
import com.vehiqon.features.carmgmt.dto.CarDto;
import com.vehiqon.features.carmgmt.dto.request.CreateCarRequest;
import com.vehiqon.features.carmgmt.dto.request.UpdateCarRequest;
import com.vehiqon.features.carmgmt.service.CarService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/api/vehicle")
@AllArgsConstructor
public class CarController {

    private final CarService carService;

    @GetMapping("user/{userId}")
    ResponseEntity<ApiResponse<CarDto.CarResponse>> getCarByUserId(
            @PathVariable("userId") String userId
    ){
        return ResponseEntity.ok().body(ApiResponse.<CarDto.CarResponse>builder()
                       // .responseCode()
                        .data(carService.getCarsByUser(UUID.fromString(userId)))
                .build());
    }

    @PostMapping("new")
    ResponseEntity<ApiResponse<CarDto.CarResponse>> addNewVehicle(
            @RequestBody CreateCarRequest request
    ){
        return ResponseEntity.ok().body(ApiResponse.<CarDto.CarResponse>builder()
                // .responseCode()
                .data(carService.create(request))
                .build());
    }

    @GetMapping("user/{userId}")
    ResponseEntity<ApiResponse<CarDto.CarResponse>> editCarByUserId(
            @PathVariable("userId") String userId,
            @RequestBody UpdateCarRequest request
    ){
        return ResponseEntity.ok().body(ApiResponse.<CarDto.CarResponse>builder()
                // .responseCode()
                .data(carService.update(UUID.fromString(userId), request))
                .build());
    }
}
