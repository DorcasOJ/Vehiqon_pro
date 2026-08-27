package com.vehiqon.features.carmgmt.dto;

import com.vehiqon.features.carmgmt.enums.DocumentStatus;
import com.vehiqon.features.carmgmt.enums.DocumentType;
import com.vehiqon.features.carmgmt.enums.VerificationStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public class CarDocumentDto {
    private CarDocumentDto(){}

    public record StorageUploadResponse(
            String storagePath,
            String originalFileName,
            Long size,
            String mimeType
    ) {}

    public record CarDocumentResponse(
            UUID carId,
            DocumentType documentType,
            String documentName,
            String originalFileName,
            String contentType,
            Long fileSize,
            String documentNumber,
            LocalDateTime issuedAt,
            String issuer,
            LocalDateTime expiresAt,
            DocumentStatus documentStatus,
            VerificationStatus verificationStatus,
            UUID verifiedBy,
            LocalDateTime verifiedAt,
            UUID rejectedBy,
            LocalDateTime rejectedAt,
            String downloadUrl
    ) {
        public CarDocumentResponse withDownloadUrl(String url) {
            return new CarDocumentResponse(
                    this.carId, this.documentType, this.documentName, this.originalFileName,this.contentType,
                    this.fileSize,this.documentNumber, this.issuedAt, this.issuer, this.expiresAt,
                    this.documentStatus, this.verificationStatus, this.verifiedBy, this.verifiedAt,
                    this.rejectedBy, this.rejectedAt, url
            );
        }
    }

    public record UploadCarDoc(
//          @NotNull
//          UUID carId,
          @NotNull
          DocumentType documentType,
          String documentName,
//          String originalFileName,
//          String storagePath,
//          String contentType,
//          Integer fileSize,
          String documentNumber,
          LocalDateTime issuedAt,
          String issuer,
          LocalDateTime expiresAt,
          @NotNull
          DocumentStatus documentStatus
//          boolean verified,
//          UUID verifiedBy,
//          LocalDateTime verifiedAt
    ){}

    public record UpdateCarDoc(
            DocumentType documentType,
            DocumentType documentName,
//            String originalFileName,
//            String storagePath,
//            String contentType,
//            Integer fileSize,
            String documentNumber,
            LocalDateTime issuedAt,
            String issuer,
            LocalDateTime expiresAt,
            DocumentStatus documentStatus
//            UUID verifiedBy,
//            LocalDateTime verifiedAt
    ){}
}
