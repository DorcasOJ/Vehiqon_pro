package com.vehiqon.features.carmgmt.service.impl;

import com.vehiqon.common.exception.BadRequestException;
import com.vehiqon.common.exception.ResourceNotFoundException;
import com.vehiqon.features.carmgmt.dto.CarDocumentDto;
import com.vehiqon.features.carmgmt.entities.CarDocumentEntity;
import com.vehiqon.features.carmgmt.enums.VerificationStatus;
import com.vehiqon.features.carmgmt.mapper.CarMapper;
import com.vehiqon.features.carmgmt.repository.CarDocumentRepository;
import com.vehiqon.features.carmgmt.repository.CarRepository;
import com.vehiqon.features.carmgmt.service.CarDocumentService;
import com.vehiqon.features.carmgmt.service.StorageService;
import com.vehiqon.features.onboarding.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarDocumentServiceImp implements CarDocumentService {
    private final CarDocumentRepository documentRepository;
    private final CarRepository carRepository;
    private final CarMapper carMapper;
    private final AuthService authService;
    private final StorageService storageService;
    @Value("${SIGNED_URL_DURATION_IN_HOURS:1}")
    private long signedDurationHours;

    private Duration getSignedUrlDuration() {
        return Duration.ofHours(signedDurationHours);
    }

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp",
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    );

    @Override
    @Transactional
    public Page<CarDocumentDto.CarDocumentResponse> getAllDocumentsForAdmin(Pageable pageable) {
        Page<CarDocumentEntity> docPage = documentRepository.findAllByDeletedFalse(pageable);
        return docPage.map(doc -> {
            String signedUrl = storageService.createSignedUrl(doc.getStoragePath(), getSignedUrlDuration());
            return carMapper.toCarDocResponse(doc).withDownloadUrl(signedUrl);
        });
    }

    @Override
    @Transactional
    public CarDocumentDto.CarDocumentResponse getDocumentForAdmin(UUID documentId) {
        CarDocumentEntity docPage = documentRepository.findByIdAndDeletedFalse(documentId).orElseThrow(
                ()-> new ResourceNotFoundException("CarDocument", documentId)
        );
        String signedUrl = storageService.createSignedUrl(docPage.getStoragePath(), getSignedUrlDuration());
        return carMapper.toCarDocResponse(docPage).withDownloadUrl(signedUrl);
    }

    @Override
    public Page<CarDocumentDto.CarDocumentResponse> getUserCarDocumentForAdmin(UUID userId, Pageable pageable) {
        Page<CarDocumentEntity> docPage = documentRepository.findDocumentsByUserId(userId, pageable);
        return docPage.map(doc -> {
            String signedUrl = storageService.createSignedUrl(doc.getStoragePath(), getSignedUrlDuration());
            return carMapper.toCarDocResponse(doc).withDownloadUrl(signedUrl);
        });
    }

    @Override
    @Transactional
    public Page<CarDocumentDto.CarDocumentResponse> getCarDocuments(UUID carId, Pageable pageable) {
        validateCarOwnership(carId,getCurrentUser());
        Page<CarDocumentEntity> docPage = documentRepository.findAllByCarIdAndDeletedFalse(carId, pageable);
        return docPage.map(doc -> {
            String signedUrl = storageService.createSignedUrl(doc.getStoragePath(), getSignedUrlDuration());
            return carMapper.toCarDocResponse(doc).withDownloadUrl(signedUrl);
        });
    }

    @Override
    @Transactional
    public CarDocumentDto.CarDocumentResponse getCarDocument(UUID carId, UUID documentId) {
        validateCarOwnership(carId, getCurrentUser());
        CarDocumentEntity documentEntity = documentRepository.findByIdAndCarIdAndDeletedFalse(documentId, carId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("CarDocuments", documentId )
                );
        String signedUrl = storageService.createSignedUrl(documentEntity.getStoragePath(), getSignedUrlDuration());
        return carMapper.toCarDocResponse(documentEntity).withDownloadUrl(signedUrl);
    }


    @Override
    @Transactional
    public List<CarDocumentDto.CarDocumentResponse> getCarDeletedDocuments(UUID carId) {
        validateCarOwnership(carId, getCurrentUser());
        List<CarDocumentEntity> documentEntity = documentRepository.findByCarIdAndDeletedTrue(carId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Documents", carId)
                );
        return documentEntity.stream().map(
                carMapper::toCarDocResponse
        ).toList();
    }


    @Override
    @Transactional
    public CarDocumentDto.CarDocumentResponse uploadDocument(UUID carId, CarDocumentDto.UploadCarDoc request, MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("File cannot be empty");
        }
        if (!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
            throw new BadRequestException("Unsupported file type: " + file.getContentType());
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException("File size cannot exceed 5MB");
        }
        UUID userId = getCurrentUser();
        validateCarOwnership(carId, userId);
        String filename = UUID.randomUUID() + "-" + sanitizeFileName(file.getOriginalFilename());
        String storagePath = String.format("users/%s/cars/%s/documents/%s", userId, carId, filename);
        CarDocumentDto.StorageUploadResponse uploadResponse = storageService.upload(storagePath, file);
        CarDocumentEntity carDocEntity = carMapper.toCarDocEntity(request);
        carDocEntity.setCarId(carId);
        carDocEntity.setStoragePath(storagePath);
        carDocEntity.setOriginalFileName(uploadResponse.originalFileName());
        carDocEntity.setContentType(uploadResponse.mimeType());
        carDocEntity.setFileSize(uploadResponse.size());
        CarDocumentEntity saved = documentRepository.save(carDocEntity);
        String signedUrl = storageService.createSignedUrl(saved.getStoragePath(), getSignedUrlDuration());
        return carMapper.toCarDocResponse(saved).withDownloadUrl(signedUrl);
    }


    @Override
    @Transactional
    public CarDocumentDto.CarDocumentResponse updateDocument(UUID carId, UUID documentId, CarDocumentDto.UpdateCarDoc request) {
        validateCarOwnership(carId, getCurrentUser());
        CarDocumentEntity documentEntity = documentRepository.findByIdAndCarIdAndDeletedFalse(documentId, carId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("CarDocuments", documentId)
                );
        carMapper.updateCarDocEntity(request, documentEntity);
        CarDocumentEntity saved = documentRepository.save(documentEntity);
        String signedUrl = storageService.createSignedUrl(documentEntity.getStoragePath(), getSignedUrlDuration());
        return carMapper.toCarDocResponse(saved).withDownloadUrl(signedUrl);
    }

    @Override
    @Transactional
    public CarDocumentDto.CarDocumentResponse replaceDocumentFile(UUID carId, UUID documentId, MultipartFile file) {
        UUID userId = getCurrentUser();
        validateCarOwnership(carId, userId);
        CarDocumentEntity
                documentEntity = documentRepository.findByIdAndCarIdAndDeletedFalse(documentId, carId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("CarDocuments", documentId)
                );
        String oldPath = documentEntity.getStoragePath();
        String newFilename = UUID.randomUUID() + "-" + sanitizeFileName(file.getOriginalFilename());
        String storagePath = String.format("users/%s/cars/%s/documents/%s", userId, carId, newFilename);
        CarDocumentDto.StorageUploadResponse uploadResponse = storageService.replace(oldPath, storagePath, file);
        documentEntity.setStoragePath(storagePath);
        documentEntity.setOriginalFileName(uploadResponse.originalFileName());
        documentEntity.setFileSize(uploadResponse.size());
        documentEntity.setContentType(uploadResponse.mimeType());
        CarDocumentEntity saved = documentRepository.save(documentEntity);
        String signedUrl = storageService.createSignedUrl(storagePath, getSignedUrlDuration());
        return carMapper.toCarDocResponse(saved).withDownloadUrl(signedUrl);
    }

    @Override
    @Transactional
    public void deleteDocument(UUID carId, UUID documentId) {
        UUID userId = getCurrentUser();
        validateCarOwnershipAndAdmin(carId, userId);
        CarDocumentEntity documentEntity = documentRepository.findByIdAndCarIdAndDeletedFalse(documentId, carId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("CarDocuments", documentId)
                );
        documentEntity.softDelete(userId);
        documentRepository.save(documentEntity);
    }

    @Override
    @Transactional
    public void deleteDocuments(UUID carId, List<UUID> documentIds) {
        if (documentIds == null || documentIds.isEmpty()) return;
        UUID userId = getCurrentUser();
        validateCarOwnershipAndAdmin(carId, userId);
        List<CarDocumentEntity> docs = documentRepository.findAllByIdInAndCarIdAndDeletedFalse(documentIds, carId);
        docs.forEach(doc -> doc.softDelete(userId));
        documentRepository.saveAll(docs);
    }

    @Override
    @Transactional
    public CarDocumentDto.CarDocumentResponse restoreDocument(UUID carId, UUID documentId) {
        validateCarOwnershipAndAdmin(carId, getCurrentUser());
        int restored = documentRepository.restoreDocument(documentId, carId);
        if (restored == 0) {
            throw new ResourceNotFoundException("CarDocument", documentId);
        }
        CarDocumentEntity documentEntity = documentRepository.findByIdAndCarIdAndDeletedFalse(documentId, carId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("CarDocument", documentId)
                );
        String signedUrl = storageService.createSignedUrl(documentEntity.getStoragePath(), getSignedUrlDuration());
        return carMapper.toCarDocResponse(documentEntity).withDownloadUrl(signedUrl);
    }

    @Override
    @Transactional
    public void restoreDocuments(UUID carId, List<UUID> documentIds) {
        if (documentIds == null || documentIds.isEmpty()) return;
        validateCarOwnershipAndAdmin(carId, getCurrentUser());
        int restored = documentRepository.restoreDocuments(documentIds, carId);
        if (restored == 0) {
            throw new ResourceNotFoundException("CarDocument", documentIds);
        }
    }

    @Override
    public CarDocumentDto.CarDocumentResponse verifyDocument(UUID carId, UUID documentId) {
        LocalDateTime now = LocalDateTime.now();
        if(!authService.isAdmin()){
            throw new BadRequestException("Unauthorised user");
        }
        CarDocumentEntity documentEntity = documentRepository.findByIdAndCarIdAndDeletedFalse(documentId, carId)
            .orElseThrow(
                    () -> new ResourceNotFoundException("CarDocument", documentId)
            );
        documentEntity.setVerificationStatus(VerificationStatus.APPROVED);
        documentEntity.setVerifiedBy(getCurrentUser());
        documentEntity.setVerifiedAt(now);
        CarDocumentEntity saved = documentRepository.save(documentEntity);
        String signedUrl = storageService.createSignedUrl(documentEntity.getStoragePath(), getSignedUrlDuration());

        return carMapper.toCarDocResponse(saved).withDownloadUrl(signedUrl);

    }

    @Override
    public CarDocumentDto.CarDocumentResponse rejectDocument(UUID carId, UUID documentId, String reason) {
        LocalDateTime now = LocalDateTime.now();
        if(!authService.isAdmin()){
            throw new BadRequestException("Unauthorised user");
        }
        CarDocumentEntity documentEntity = documentRepository.findByIdAndCarIdAndDeletedFalse(documentId, carId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Error retrieving restored document")
                );
        documentEntity.setVerificationStatus(VerificationStatus.REJECTED);
        documentEntity.setRejectedBy(getCurrentUser());
        documentEntity.setRejectionReason(reason);
        documentEntity.setRejectedAt(now);
        CarDocumentEntity saved = documentRepository.save(documentEntity);
        return carMapper.toCarDocResponse(saved);
    }

    private UUID getCurrentUser() {
        return authService.getAuthenticatedUser().user().getId();
    }

    private void validateCarOwnership(UUID carId, UUID userId) {
        if (!carRepository.existsByIdAndUserId(carId, userId)) {
            throw new ResourceNotFoundException("Car", carId);
        }
    }

    private void validateCarOwnershipAndAdmin(UUID carId, UUID userId) {
        if (authService.isAdmin()) return;
        if (!carRepository.existsByIdAndUserId(carId, userId)) {
            throw new ResourceNotFoundException("Car not found or unauthorised access");
        }
    }

    private String sanitizeFileName(String originalFilename) {
        if (originalFilename == null) return "file";
        return originalFilename.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
