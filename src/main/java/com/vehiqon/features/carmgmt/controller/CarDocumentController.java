package com.vehiqon.features.carmgmt.controller;

import com.vehiqon.common.api.dto.response.ApiResponse;
import com.vehiqon.common.enums.EntityEnum;
import com.vehiqon.common.api.mapper.ApiResponseMapper;
import com.vehiqon.features.carmgmt.dto.CarDocumentDto;
import com.vehiqon.features.carmgmt.service.CarDocumentService;
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
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/vehicles/documents")
@AllArgsConstructor
@RestController
@SecurityRequirement(name = "bearerAuth")
public class CarDocumentController {
    private final CarDocumentService documentService;
    private final ApiResponseMapper apiResponseMapper;




    /**
     *
     * @return list of users vehicles documents
     */
    @AnalyticsAction(
            value = EventType.VIEW_VEHICLE_DOCUMENT,
            entityIdSource = EntityIdSource.PATH_VARIABLE,
            entityIdParam = "carId"
    )
    @AuditAction(
            value = AuditActionType.VIEW_VEHICLE_DOCUMENT,
            entityType = EntityEnum.VEHICLE,
            entityIdSource = EntityIdSource.PATH_VARIABLE,
            entityIdParam = "carId"
    )
    @GetMapping("/{carId}")
    ResponseEntity<ApiResponse<Page<CarDocumentDto.CarDocumentResponse>>> getDocuments(
            @PathVariable UUID carId,
            @Parameter(hidden = true)
            @PageableDefault(page = 0, size = 15, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable

    ){
        return ResponseEntity.ok().body(
                apiResponseMapper.toResponse(
                        documentService.getCarDocuments(carId, extractPageable(pageable)))
        );
    }

    /**
     *
     * @return list of users deleted vehicles documents
     */
    @GetMapping("/deleted/{carId}")
    ResponseEntity<ApiResponse<List<CarDocumentDto.CarDocumentResponse>>> getDeletedDocuments(
            @PathVariable UUID carId
    ){
        return ResponseEntity.ok().body(
                apiResponseMapper.toResponse(
                        documentService.getCarDeletedDocuments(carId))
        );
    }


    /**
     *
     * @return one of users vehicles documents
     */
    @AnalyticsAction(
            value = EventType.VIEW_VEHICLE_DOCUMENT,
            entityIdSource = EntityIdSource.PATH_VARIABLE,
            entityIdParam = "documentId"
    )
    @AuditAction(
            value = AuditActionType.VIEW_VEHICLE_DOCUMENT,
            entityType = EntityEnum.VEHICLE,
            entityIdSource = EntityIdSource.PATH_VARIABLE,
            entityIdParam = "documentId"
    )
    @GetMapping("/{carId}/{documentId}")
    ResponseEntity<ApiResponse<CarDocumentDto.CarDocumentResponse>> getDocument(
            @PathVariable UUID carId,
            @PathVariable UUID documentId
    ){
        return ResponseEntity.ok().body(
                apiResponseMapper.toResponse(
                        documentService.getCarDocument(carId, documentId))
        );
    }


    /**
     *
     * @return a newly uploaded car document
     */
    @AnalyticsAction(
            value = EventType.VEHICLE_DOCUMENT_UPLOADED,
            entityIdSource = EntityIdSource.PATH_VARIABLE
    )
    @AuditAction(
            value = AuditActionType.VEHICLE_DOCUMENT_UPLOADED,
            entityType = EntityEnum.VEHICLE,
            entityIdSource = EntityIdSource.PATH_VARIABLE,
            entityIdParam = ""
    )
    @PostMapping(
            value = "/{carId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<CarDocumentDto.CarDocumentResponse>> uploadCarDoc(
            @PathVariable("carId") UUID carId,
            @Valid @ModelAttribute CarDocumentDto.UploadCarDoc request,
            @RequestPart("file") MultipartFile file
    ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(apiResponseMapper.toResponse(documentService.uploadDocument(carId, request, file))
                );
    }

    /**
     *
     * @return the updated car document
     */
    @AnalyticsAction(
            value = EventType.VEHICLE_DOCUMENT_UPDATED,
            entityIdSource = EntityIdSource.PATH_VARIABLE,
            entityIdParam = "documentId"
    )
    @AuditAction(
            value = AuditActionType.VEHICLE_DOCUMENT_UPDATED,
            entityType = EntityEnum.VEHICLE,
            entityIdSource = EntityIdSource.PATH_VARIABLE,
            entityIdParam = "documentId"

    )
    @PatchMapping("/{carId}/{documentId}")
    public ResponseEntity<ApiResponse<CarDocumentDto.CarDocumentResponse>> updateCarDocMetadataOnly(
            @PathVariable("carId") UUID carId,
            @PathVariable("documentId") UUID documentId,
            @Valid @RequestBody CarDocumentDto.UpdateCarDoc request
    ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(apiResponseMapper.toResponse(documentService.updateDocument(carId, documentId, request))
                );
    }

    /**
     *
     * @return the updated car document
     */
    @AnalyticsAction(
            value = EventType.VEHICLE_DOCUMENT_UPDATED,
            entityIdSource = EntityIdSource.PATH_VARIABLE,
            entityIdParam = "documentId"
    )
    @AuditAction(
            value = AuditActionType.VEHICLE_DOCUMENT_UPDATED,
            entityType = EntityEnum.VEHICLE,
            entityIdSource = EntityIdSource.PATH_VARIABLE,
            entityIdParam = "documentId"

    )
    @PutMapping(
            value="/{carId}/{documentId}/file",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<CarDocumentDto.CarDocumentResponse>> replaceCarDocFileOnly(
            @PathVariable("carId") UUID carId,
            @PathVariable("documentId") UUID documentId,
            @RequestPart("file") MultipartFile file
    ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(apiResponseMapper.toResponse(documentService.replaceDocumentFile(carId, documentId, file))
                );
    }

    /**
     *
     * @return a string upon deletion of car document
     */
    @AnalyticsAction(
            value = EventType.VEHICLE_DOCUMENT_DELETED,
            entityIdSource = EntityIdSource.PATH_VARIABLE,
            entityIdParam = "documentId"
    )
    @AuditAction(
            value = AuditActionType.VEHICLE_DOCUMENT_DELETED,
            entityType = EntityEnum.VEHICLE,
            entityIdSource = EntityIdSource.PATH_VARIABLE,
            entityIdParam = "documentId"

    )
    @DeleteMapping("/{carId}/{documentId}")
    public ResponseEntity<ApiResponse<String>> deleteCarDoc(
            @PathVariable("carId") UUID carId,
            @PathVariable("documentId") UUID documentId
    ){
        documentService.deleteDocument(carId, documentId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(apiResponseMapper.toResponse("Document deleted successfully")
                );
    }

    /**
     *
     * @return a car document upon restoration
     */
    @AnalyticsAction(
            value = EventType.VEHICLE_DOCUMENT_RESTORED,
            entityIdSource = EntityIdSource.PATH_VARIABLE,
            entityIdParam = "documentId"
    )
    @AuditAction(
            value = AuditActionType.VEHICLE_DOCUMENT_RESTORED,
            entityType = EntityEnum.VEHICLE,
            entityIdSource = EntityIdSource.PATH_VARIABLE,
            entityIdParam = "documentId"

    )
    @PostMapping("/{carId}/{documentId}/restore")
    public ResponseEntity<ApiResponse<CarDocumentDto.CarDocumentResponse>> restoreCarDoc(
            @PathVariable("carId") UUID carId,
            @PathVariable("documentId") UUID documentId
    ){
        ;
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(apiResponseMapper.toResponse(documentService.restoreDocument(carId, documentId))
                );
    }


    /**
     *
     * @return a string upon deletion of car document
     */
    @AnalyticsAction(
            value = EventType.VEHICLE_DOCUMENT_DELETED,
            entityIdSource = EntityIdSource.PATH_VARIABLE,
            entityIdParam = "documentId"
    )
    @AuditAction(
            value = AuditActionType.VEHICLE_DOCUMENT_DELETED,
            entityType = EntityEnum.VEHICLE,
            entityIdSource = EntityIdSource.PATH_VARIABLE,
            entityIdParam = "documentId"
    )
    @DeleteMapping("/{carId}/delete")
    public ResponseEntity<ApiResponse<String>> deleteAllCarDoc(
            @PathVariable("carId") UUID carId,
            @RequestBody List<UUID> documentIds
    ){
        documentService.deleteDocuments(carId, documentIds);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(apiResponseMapper.toResponse("Document deleted successfully")
                );
    }

    /**
     *
     * @return a string upon deletion of car document
     */
    @AnalyticsAction(
            value = EventType.VEHICLE_DOCUMENT_RESTORED,
            entityIdSource = EntityIdSource.PATH_VARIABLE,
            entityIdParam = "documentId"
    )
    @AuditAction(
            value = AuditActionType.VEHICLE_DOCUMENT_RESTORED,
            entityType = EntityEnum.VEHICLE,
            entityIdSource = EntityIdSource.PATH_VARIABLE,
            entityIdParam = "documentId"
    )
    @PostMapping("/{carId}/restore")
    public ResponseEntity<ApiResponse<String>> restoreAllCarDoc(
            @PathVariable("carId") UUID carId,
            @RequestBody List<UUID> documentIds
    ){
        documentService.restoreDocuments(carId, documentIds);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(apiResponseMapper.toResponse("Document restored  successfully")
                );
    }


    private Pageable extractPageable(Pageable pageable) {
        boolean hasInvalidSort = pageable.getSort().stream()
                .anyMatch(order -> order.getProperty().equalsIgnoreCase("string"));
        if (hasInvalidSort) {
            return PageRequest.of(
                    pageable.getPageNumber(), pageable.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "createdAt")
            );
        }
        return pageable;

    }


}
