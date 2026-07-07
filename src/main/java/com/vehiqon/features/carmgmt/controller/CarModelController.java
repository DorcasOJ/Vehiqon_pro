package com.vehiqon.features.carmgmt.controller;

import com.vehiqon.common.dto.response.ApiResponse;
import com.vehiqon.common.mapper.ApiResponseMapper;
import com.vehiqon.features.carmgmt.dto.CarModelDto;
import com.vehiqon.features.carmgmt.service.CarModelService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/models")
@AllArgsConstructor
@RestController
public class CarModelController {
    private final CarModelService carModelService;
    private final ApiResponseMapper apiResponseMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CarModelDto.CarModelResponse>>> getAllModels() {

        return ResponseEntity.ok(
               apiResponseMapper.toResponse(carModelService.getAllModels())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CarModelDto.CarModelResponse>> getModel(
            @PathVariable UUID id) {

        return ResponseEntity.ok(apiResponseMapper.toResponse(carModelService.getModel(id))
          );
    }

    @GetMapping("/brand/{brandId}")
    public ResponseEntity<ApiResponse<List<CarModelDto.CarModelResponse>>> getModelsByBrand(
            @PathVariable UUID brandId) {

        return ResponseEntity.ok(
                apiResponseMapper.toResponse(carModelService.getModelsByBrand(brandId))
        );
    }
}
