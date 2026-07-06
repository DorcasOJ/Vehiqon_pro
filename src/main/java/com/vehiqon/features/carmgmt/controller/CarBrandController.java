package com.vehiqon.features.carmgmt.controller;

import com.vehiqon.common.dto.response.ApiResponse;
import com.vehiqon.common.mapper.ApiResponseMapper;
import com.vehiqon.features.carmgmt.dto.CarBrandDto;
import com.vehiqon.features.carmgmt.dto.CarModelDto;
import com.vehiqon.features.carmgmt.service.CarBrandService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/brands")
@AllArgsConstructor
@RestController
public class CarBrandController {
    private final CarBrandService brandService;
    private final ApiResponseMapper apiResponseMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CarBrandDto.CarBrandResponse>>> getBrands() {
        return ResponseEntity.ok(
                apiResponseMapper.toResponse(brandService.getAllBrands())
        );
    }

    @GetMapping("{name}")
    public ResponseEntity<ApiResponse<CarBrandDto.CarBrandResponse>> getBrandByName(
            @PathVariable String name
    ) {
        return ResponseEntity.ok(
                apiResponseMapper.toResponse(brandService.getBrandByName(name.toLowerCase()))
        );
    }

    @GetMapping("/{brandId}/models")
    public ResponseEntity<ApiResponse<List<CarModelDto.CarModelResponse>>> getModels
            ( @PathVariable UUID brandId) {
        return ResponseEntity.ok(
               apiResponseMapper.toResponse(brandService.getModelsByBrand(brandId))
        );
    }

}
