package com.vehiqon.features.carmgmt.controller;

import com.vehiqon.common.api.dto.response.ApiResponse;
import com.vehiqon.common.api.mapper.ApiResponseMapper;
import com.vehiqon.features.carmgmt.dto.CarBrandDto;
import com.vehiqon.features.carmgmt.dto.CarModelDto;
import com.vehiqon.features.carmgmt.service.CarBrandModelService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/vehicleType")
@AllArgsConstructor
@RestController
@SecurityRequirement(name = "bearerAuth")
public class CarBrandModelController {
    private final CarBrandModelService brandService;
    private final ApiResponseMapper apiResponseMapper;

    @GetMapping("/brands")
    public ResponseEntity<ApiResponse<List<CarBrandDto.CarBrandResponse>>> getBrands() {
        return ResponseEntity.ok(
                apiResponseMapper.toResponse(brandService.getAllBrands())
        );
    }

    @GetMapping("/brands/{name}")
    public ResponseEntity<ApiResponse<CarBrandDto.CarBrandResponse>> getBrandByName(
            @PathVariable String name
    ) {
        return ResponseEntity.ok(
                apiResponseMapper.toResponse(brandService.getBrandByName(name.toLowerCase()))
        );
    }

    @GetMapping("/brands/{brandId}/models")
    public ResponseEntity<ApiResponse<List<CarModelDto.CarModelResponse>>> getModels
            ( @PathVariable UUID brandId) {
        return ResponseEntity.ok(
               apiResponseMapper.toResponse(brandService.getModelsByBrandId(brandId))
        );
    }

    @GetMapping("/model")
    public ResponseEntity<ApiResponse<List<CarModelDto.CarModelResponse>>> getAllModels() {

        return ResponseEntity.ok(
                apiResponseMapper.toResponse(brandService.getAllModels())
        );
    }

    @GetMapping("/model/{id}")
    public ResponseEntity<ApiResponse<CarModelDto.CarModelResponse>> getModel(
            @PathVariable UUID id) {

        return ResponseEntity.ok(apiResponseMapper.toResponse(brandService.getModel(id))
        );
    }

//    @GetMapping("/model/brand/{brandId}")
//    public ResponseEntity<ApiResponse<List<CarModelDto.CarModelResponse>>> getModelsByBrand(
//            @PathVariable UUID brandId) {
//
//        return ResponseEntity.ok(
//                apiResponseMapper.toResponse(brandService.getModelsByBrand(brandId))
//        );
//    }

}
