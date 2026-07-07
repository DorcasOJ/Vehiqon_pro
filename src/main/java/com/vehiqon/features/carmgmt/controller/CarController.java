package com.vehiqon.features.carmgmt.controller;

import com.vehiqon.common.dto.response.ApiResponse;
import com.vehiqon.common.exception.BadRequestException;
import com.vehiqon.common.exception.BusinessException;
import com.vehiqon.features.carmgmt.dto.CarDto;
import com.vehiqon.features.carmgmt.dto.request.CreateCarRequest;
import com.vehiqon.features.carmgmt.dto.request.UpdateCarRequest;
import com.vehiqon.features.carmgmt.service.CarService;
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
public class CarController {

    private final CarService carService;


    //    register a vehicle
    @PostMapping("/new")
    public ResponseEntity<ApiResponse<CarDto.CarResponse>> registerCar(
            @Valid @RequestBody CarDto.CreateCarRequest request
    ){
        System.out.println("registering new car");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<CarDto.CarResponse>builder()
                        .success(true)
                        .data(carService.registerCar(request))
                        .build());
    }

//    get list of users vehicles
    @GetMapping()
    ResponseEntity<ApiResponse<List<CarDto.CarResponse>>> getCarByUserId(
    ){
        return ResponseEntity.ok()
                .body(ApiResponse.<List<CarDto.CarResponse>>builder()
                        .success(true)
                        .data(carService.getMyCars())
                .build());
    }

//    get one vehicle of a vehicle
    @GetMapping("{carId}")
    ResponseEntity<ApiResponse<CarDto.CarResponse>> getCar(
            @PathVariable UUID carId
    ){
        return ResponseEntity.ok()
                .body(ApiResponse.<CarDto.CarResponse>builder()
                        .success(true)
                        .data(carService.getCar(carId))
                        .build());
    }

    @PutMapping("{carId}")
    ResponseEntity<ApiResponse<CarDto.CarResponse>> updateCar(
            @PathVariable UUID carId,
            @Valid @RequestBody CarDto.CreateCarRequest request
    ){
        return ResponseEntity.ok().body(ApiResponse.<CarDto.CarResponse>builder()
                // .responseCode()
                        .success(true)
                .data(carService.update(carId,request ))
                .build());
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
