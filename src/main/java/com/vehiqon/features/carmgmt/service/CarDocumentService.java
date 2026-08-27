package com.vehiqon.features.carmgmt.service;

import com.vehiqon.features.carmgmt.dto.CarDocumentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface CarDocumentService {

    Page<CarDocumentDto.CarDocumentResponse> getAllDocumentsForAdmin(
            Pageable pageable
    );


    Page<CarDocumentDto.CarDocumentResponse> getCarDocuments(
            UUID carId,
            Pageable pageable
    );

    CarDocumentDto.CarDocumentResponse getDocumentForAdmin(
           UUID documentId
    );

    Page<CarDocumentDto.CarDocumentResponse> getUserCarDocumentForAdmin(
            UUID userId,
            Pageable pageable
    );

    List<CarDocumentDto.CarDocumentResponse> getCarDeletedDocuments(UUID carId);

    CarDocumentDto.CarDocumentResponse getCarDocument(
            UUID carId,
            UUID documentId
    );
    CarDocumentDto.CarDocumentResponse uploadDocument(
            UUID carId,
            CarDocumentDto.UploadCarDoc request,
            MultipartFile file
    );

    CarDocumentDto.CarDocumentResponse updateDocument(
            UUID carId,
            UUID documentId,
            CarDocumentDto.UpdateCarDoc request
    );

    CarDocumentDto.CarDocumentResponse replaceDocumentFile(
            UUID carId,
            UUID documentId,
            MultipartFile file
    );

    void deleteDocument(
            UUID carId,
            UUID documentId
    );

    void deleteDocuments(
            UUID carId,
            List<UUID> documentIds
    );

    CarDocumentDto.CarDocumentResponse restoreDocument(
            UUID carId,
            UUID documentId
    );

    void restoreDocuments(
            UUID carId,
            List<UUID> documentIds
    );

    CarDocumentDto.CarDocumentResponse verifyDocument(
            UUID carId,
            UUID documentId
    );


    CarDocumentDto.CarDocumentResponse rejectDocument(
            UUID carId,
            UUID documentId,
            String reason
    );

}