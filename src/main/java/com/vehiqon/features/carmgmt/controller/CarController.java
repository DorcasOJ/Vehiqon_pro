package com.vehiqon.features.carmgmt.controller;

import com.vehiqon.common.dto.response.ApiResponse;
import com.vehiqon.common.mapper.ApiResponseMapper;
import com.vehiqon.features.carmgmt.dto.request.CreateCarRequest;
import com.vehiqon.features.carmgmt.dto.response.CarResponse;
import com.vehiqon.features.carmgmt.service.CarBrandService;
import com.vehiqon.features.carmgmt.service.impl.CarServiceImpl;
import com.vehiqon.features.onboarding.entity.UserEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {
    private final CarServiceImpl carService;
    private final ApiResponseMapper apiResponseMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<CarResponse>> registerCar(
            @Valid @RequestBody CreateCarRequest request, Authentication authentication
            ) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        return ResponseEntity.ok(apiResponseMapper.toResponse(
                carService.registerCar(user, request)
        ));
    }
}
