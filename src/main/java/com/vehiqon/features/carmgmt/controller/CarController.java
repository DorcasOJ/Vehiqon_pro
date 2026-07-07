package com.vehiqon.features.carmgmt.controller;

import com.vehiqon.common.dto.response.ApiResponse;
import com.vehiqon.common.exception.BadRequestException;
import com.vehiqon.common.exception.BusinessException;
import com.vehiqon.common.mapper.ApiResponseMapper;
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
    private final ApiResponseMapper apiResponseMapper;


    //    register a vehicle
    @PostMapping("/new")
    public ResponseEntity<ApiResponse<CarDto.CarResponse>> registerCar(
            @Valid @RequestBody CarDto.CreateCarRequest request
    ){
        System.out.println("registering new car");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(apiResponseMapper.toResponse(carService.registerCar(request))
                );
    }

//    get list of users vehicles
    @GetMapping()
    ResponseEntity<ApiResponse<List<CarDto.CarResponse>>> getCarByUserId(
    ){
        return ResponseEntity.ok()
                .body(apiResponseMapper.toResponse(carService.getMyCars()));
    }

//    get one vehicle of a vehicle
    @GetMapping("{carId}")
    ResponseEntity<ApiResponse<CarDto.CarResponse>> getCar(
            @PathVariable UUID carId
    ){
        return ResponseEntity.ok()
                .body(apiResponseMapper.toResponse(carService.getCar(carId)));
    }

    @PutMapping("{carId}")
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
