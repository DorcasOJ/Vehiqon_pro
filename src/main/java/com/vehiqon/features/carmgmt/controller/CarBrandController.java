package com.vehiqon.features.carmgmt.controller;

import com.vehiqon.common.dto.response.ApiResponse;
import com.vehiqon.common.mapper.ApiResponseMapper;
import com.vehiqon.features.carmgmt.dto.response.CarBrandResponse;
import com.vehiqon.features.carmgmt.dto.response.CarModelResponse;
import com.vehiqon.features.carmgmt.service.CarBrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class CarBrandController {
    private final CarBrandService brandService;
    private final ApiResponseMapper apiResponseMapper;

//    @GetMapping
//    public ResponseEntity<ApiResponse<List<CarBrandResponse>>> getBrands() {
//        return ResponseEntity.ok(
//                apiResponseMapper.toResponse(brandService.getAllBrands())
//        );
//    }
//
//    @GetMapping("/{brandId}/models")
//    public ResponseEntity<ApiResponse<List<CarModelResponse>>> getModels
//            ( @PathVariable UUID brandId) {
//        return ResponseEntity.ok(
//               apiResponseMapper.toResponse(brandService.getModelsByBrand(brandId))
//        );
//    }
//
//    @GetMapping
//    public ResponseEntity<ApiResponse<List<BrandDto.BrandResponse>>> getBrands() {
//        return ResponseEntity.ok(
//                ApiResponse.<List<BrandDto.BrandResponse>>builder()
//                        .responseCode(AccountUtils.SUCCESS_CODE)
//                        .responseMessage(AccountUtils.SUCCESS_MESSAGE)
//                        .data(brandService.getBrands())
//                        .build()
//        );
//    }
//
//    @GetMapping("/{brandId}/models")
//    public ResponseEntity<ApiResponse<List<ModelDto.ModelResponse>>> getBrandModels(
//            @PathVariable UUID brandId) {
//
//        return ResponseEntity.ok(
//                ApiResponse.<List<ModelDto.ModelResponse>>builder()
//                        .responseCode(AccountUtils.SUCCESS_CODE)
//                        .responseMessage(AccountUtils.SUCCESS_MESSAGE)
//                        .data(brandService.getBrandModels(brandId))
//                        .build()
//        );
//    }
}
