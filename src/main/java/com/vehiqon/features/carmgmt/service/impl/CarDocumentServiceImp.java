package com.vehiqon.features.carmgmt.service;

import com.vehiqon.common.exception.ResourceNotFoundException;
import com.vehiqon.config.SupabaseProperties;
import com.vehiqon.features.carmgmt.dto.CarDocumentDto;
import com.vehiqon.features.carmgmt.entities.CarDocumentEntity;
import com.vehiqon.features.carmgmt.mapper.CarMapper;
import com.vehiqon.features.carmgmt.repository.CarDocumentRepository;
import com.vehiqon.features.onboarding.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarDocumentService {
    private final CarDocumentRepository documentRepository;
    private final RestClient restClient;
    private final SupabaseProperties properties;
    private final CarMapper carMapper;
    private final AuthService authService;

    public void getCarDocuments(UUID carId) {
        List<CarDocumentEntity> documentEntities = documentRepository.findByCarId(carId).orElseThrow(
                () -> new ResourceNotFoundException("Car Documents not found")
        );
    }

    public void getCarDocument(UUID carId, UUID documentId) {
        CarDocumentEntity documentEntity = documentRepository.findByIdAndCarId(documentId, carId)
                .orElseThrow(
                () -> new ResourceNotFoundException("Car Documents not found")
        );
    }

    public CarDocumentEntity newDocument(CarDocumentDto.UploadCarDoc request, MultipartFile file){
        try {
            UUID documentId = UUID.randomUUID();
            String storagePath = uploadFile(request.carId(), file, documentId);
            CarDocumentEntity carDocEntity = carMapper.toCarDocEntity(request);
            carDocEntity.setId(documentId);
            carDocEntity.setStoragePath(storagePath);
            carDocEntity.setSize(file.getSize());
            carDocEntity.setOriginalFileName(file.getOriginalFilename());
            carDocEntity.setMimeType(file.getContentType());
            return documentRepository.save(carDocEntity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public CarDocumentEntity updateDocument(UUID documentId, CarDocumentDto.UploadCarDoc request) {
        CarDocumentEntity documentEntity = documentRepository.findByIdAndCarId(documentId, request.carId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Car Documents not found")
                );
        carMapper.updateCarDocEntity(request, documentEntity);
        return documentRepository.save(documentEntity);
    }

    public void deleteCarDocument(UUID documentId, UUID carId) {
        UUID userId = authService.getAuthenticatedUser().user().getId();
        CarDocumentEntity documentEntity = documentRepository.findByIdAndCarId(documentId, carId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Car Documents not found")
                );
        documentEntity.softDelete(userId);
        documentRepository.save(documentEntity);
    }

    private String uploadFile(UUID carId, MultipartFile file, UUID documentId) {
        try {
            String filename = documentId + "-" + file.getOriginalFilename();
            String path = "vehicles/" + carId + "/" + filename;
            restClient.post()
                    .uri(properties.getUrl()
                    + "/storage/v1/object/" + properties.getBucket() + "/" + path)
                    .header("Authorization", "Bearer" + properties.getServiceKey())
                    .header("apiKey", properties.getServiceKey())
                    .contentType(MediaType.parseMediaType(Objects.requireNonNull(file.getContentType())))
                    .body(file.getBytes())
                    .retrieve()
                    .toBodilessEntity();
            return path;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
