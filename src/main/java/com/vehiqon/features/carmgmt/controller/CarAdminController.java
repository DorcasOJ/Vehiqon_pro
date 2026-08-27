package com.vehiqon.features.carmgmt.controller;

import com.vehiqon.common.dto.response.ApiResponse;
import com.vehiqon.common.enums.EntityEnum;
import com.vehiqon.common.dto.mapper.ApiResponseMapper;
import com.vehiqon.features.carmgmt.dto.CarDocumentDto;
import com.vehiqon.features.carmgmt.dto.CarDto;
import com.vehiqon.features.carmgmt.dto.response.CarDetailsResponse;
import com.vehiqon.features.carmgmt.enums.CarStatus;
import com.vehiqon.features.carmgmt.service.CarDocumentService;
import com.vehiqon.features.carmgmt.service.CarService;
import com.vehiqon.features.insights.analytics.enums.EntityIdSource;
import com.vehiqon.features.insights.analytics.enums.EventType;
import com.vehiqon.features.insights.analytics.service.around.AnalyticsAction;
import com.vehiqon.features.insights.auditLog.enums.AuditActionType;
import com.vehiqon.features.insights.auditLog.service.around.AuditAction;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
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
    private final CarDocumentService carDocumentService;
    private final ApiResponseMapper apiResponseMapper;

    @GetMapping
    ResponseEntity<ApiResponse<Page<CarDetailsResponse>>> getAllCars(
            @Parameter(hidden = true)
            @PageableDefault(page = 0, size = 15, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable

    ){
        pageable = extractPageable(pageable);
        return ResponseEntity.ok()
                .body(apiResponseMapper.toResponse(carService.getAllCars(pageable)));

    }

    @GetMapping("/{carId}")
    ResponseEntity<ApiResponse<CarDetailsResponse>> getCarById(
            @Param("carId") UUID carId
    ){
        return ResponseEntity.ok()
                .body(apiResponseMapper.toResponse(carService.getCarById(carId)));

    }

    @GetMapping("users/{userId}")
    ResponseEntity<ApiResponse<Page<CarDetailsResponse>>> getAllCarByUserId(
            @PathVariable("userId") UUID userId,
            @Parameter(hidden = true)
            @PageableDefault(page = 0, size = 15, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable

    ){

        return ResponseEntity.ok()
                .body(apiResponseMapper.toResponse(carService.getCarsByUser(userId,  extractPageable(pageable)))
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


    @GetMapping("/documents")
    ResponseEntity<ApiResponse<Page<CarDocumentDto.CarDocumentResponse>>> getAllCarDocuments(
            @Parameter(hidden = true)
            @PageableDefault(page = 0, size = 15, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable

    ){
        pageable = extractPageable(pageable);
        return ResponseEntity.ok()
                .body(apiResponseMapper.toResponse(
                        carDocumentService.getAllDocumentsForAdmin(pageable)));
    }


    @GetMapping("/documents/{userId}")
    ResponseEntity<ApiResponse<Page<CarDocumentDto.CarDocumentResponse>>> getUserCarDocuments(
            @PathVariable("userId") UUID userId,
            @Parameter(hidden = true)
            @PageableDefault(page = 0, size = 15, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable

    ){
        pageable = extractPageable(pageable);
        return ResponseEntity.ok()
                .body(apiResponseMapper.toResponse(
                        carDocumentService.getUserCarDocumentForAdmin(userId, pageable)));
    }

    @GetMapping("/documents/{documentId}")
    ResponseEntity<ApiResponse<CarDocumentDto.CarDocumentResponse>> getACarDocuments(
            @PathVariable("documentId") UUID documentId,
            @Parameter(hidden = true)
            @PageableDefault(page = 0, size = 15, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable

    ){
        pageable = extractPageable(pageable);
        return ResponseEntity.ok()
                .body(apiResponseMapper.toResponse(
                        carDocumentService.getDocumentForAdmin(documentId)));
    }


    @GetMapping("/search")
    ResponseEntity<ApiResponse<Page<CarDetailsResponse>>> searchCar(
            @RequestParam String query,
            @RequestParam(required = false) UUID brandId,
            @RequestParam(required = false) CarStatus status,
            @Parameter(hidden = true)
            @PageableDefault(page = 0, size = 15, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        Page<CarDetailsResponse> carDetailsResponses = carService.searchCars(query, brandId, status, pageable);
        return ResponseEntity.ok()
                .body(apiResponseMapper.toResponse(carDetailsResponses));
    }


    @AuditAction(
            value = AuditActionType.VEHICLE_UPDATED,
            entityType = EntityEnum.VEHICLE,
            entityIdSource = EntityIdSource.PATH_VARIABLE,
            entityIdParam = "carId"
    )
    @PatchMapping("users/{carId}")
    ResponseEntity<ApiResponse<CarDto.CarResponse>> editCarByUserId(
            @PathVariable("carId") UUID carId,
            @Valid @RequestBody CarDto.UpdateCarRequest request
    ){
        return ResponseEntity.ok().body(apiResponseMapper.toResponse(carService.updateUserCar(carId, request))
        );
    }

    @AuditAction(
            value = AuditActionType.VEHICLE_DOCUMENT_VERIFIED,
            entityType = EntityEnum.VEHICLE_DOCUMENT,
            entityIdSource = EntityIdSource.PATH_VARIABLE,
            entityIdParam = "documentId"
    )
    @PostMapping("/documents/{carId}/{documentId}/verify")
    ResponseEntity<ApiResponse<CarDocumentDto.CarDocumentResponse>> verifyAACarDocument(
            @PathVariable("carId") UUID carId,
            @PathVariable("documentId") UUID documentId
            ){

        return ResponseEntity.ok()
                .body(apiResponseMapper.toResponse(
                        carDocumentService.verifyDocument(carId,documentId)));
    }


    @AuditAction(
            value = AuditActionType.VEHICLE_DOCUMENT_VERIFIED,
            entityType = EntityEnum.VEHICLE_DOCUMENT,
            entityIdSource = EntityIdSource.PATH_VARIABLE,
            entityIdParam = "documentId"
    )
    @PostMapping("/documents/{carId}/{documentId}/reject")
    ResponseEntity<ApiResponse<CarDocumentDto.CarDocumentResponse>> rejectArDocument(
            @PathVariable("carId") UUID carId,
            @PathVariable("documentId") UUID documentId,
            @RequestBody String reason
    ){

        return ResponseEntity.ok()
                .body(apiResponseMapper.toResponse(
                        carDocumentService.rejectDocument(carId,documentId, reason)));
    }


    @AuditAction(
            value = AuditActionType.VEHICLE_DELETED,
            entityType = EntityEnum.VEHICLE,
            entityIdSource = EntityIdSource.PATH_VARIABLE,
            entityIdParam = "carId"
    )
    @DeleteMapping("{carId}")
    public ResponseEntity<ApiResponse<Void>> deleteCar(
            @PathVariable UUID carId
            ) {
        carService.deleteCarByAdmin(carId);
        return ResponseEntity.noContent().build();
    }

    @AnalyticsAction(
            value = EventType.VEHICLE_RESTORED,
            entityIdSource = EntityIdSource.PATH_VARIABLE,
            entityIdParam = "carId"
    )
    @AuditAction(
            value = AuditActionType.VEHICLE_RESTORED,
            entityType = EntityEnum.VEHICLE,
            entityIdSource = EntityIdSource.PATH_VARIABLE,
            entityIdParam = "carId"
    )
    @PostMapping("/{carId}/restore")
    public ResponseEntity<ApiResponse<Void>> restoreCar(
            @PathVariable UUID carId) {
        carService.restoreCarByAdmin(carId);
        return ResponseEntity.noContent().build();
    }

    @AnalyticsAction(
            value = EventType.VEHICLE_RESTORED,
            entityIdSource = EntityIdSource.NONE
    )
    @AuditAction(
            value = AuditActionType.VEHICLE_RESTORED,
            entityType = EntityEnum.VEHICLE,
            entityIdSource = EntityIdSource.NONE
    )
    @PostMapping("/restore")
    public ResponseEntity<ApiResponse<Void>> restoreAllCars(
            @PathVariable List<UUID> carIds) {
        carService.restoreMultipleCars(carIds);
        return ResponseEntity.noContent().build();
    }


    @AuditAction(
            value = AuditActionType.VEHICLE_DELETED,
            entityType = EntityEnum.VEHICLE,
            entityIdSource = EntityIdSource.NONE
    )
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<Void>> deleteAllUserCar(
            @RequestBody List<UUID> carIds) {
        carService.deleteMultipleCarByAdmin(carIds);
        return ResponseEntity.noContent().build();
    }


//    @AuditAction(
//            value = AuditActionType.VEHICLE_DELETED,
//            entityType = EntityEnum.VEHICLE,
//            entityIdSource = EntityIdSource.NONE
//    )
//    @DeleteMapping("/delete")
//    public ResponseEntity<ApiResponse<Void>> deleteAllUserCar(
//            @RequestBody List<UUID> carIds) {
//        carService.deleteMultipleCarByAdmin(carIds);
//        return ResponseEntity.noContent().build();
//    }


    private static Pageable extractPageable(Pageable pageable) {
        boolean hasInvalidSort = pageable.getSort().stream()
                .anyMatch(order -> order.getProperty().equalsIgnoreCase("string"));
        if (hasInvalidSort) {
            pageable = PageRequest.of(
                    pageable.getPageNumber(), pageable.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "createdAt")
            );
        }
        return pageable;
    }



}
