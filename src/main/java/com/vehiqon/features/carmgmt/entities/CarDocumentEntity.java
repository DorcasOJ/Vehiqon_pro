package com.vehiqon.features.carmgmt.entities;


import com.vehiqon.common.entity.BaseWithDeleteEntity;
import com.vehiqon.features.carmgmt.enums.DocumentStatus;
import com.vehiqon.features.carmgmt.enums.DocumentType;
import com.vehiqon.features.carmgmt.enums.VerificationStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name="car_documents")
public class CarDocumentEntity extends BaseWithDeleteEntity {

    private UUID carId;

    @Enumerated(EnumType.STRING)
    private DocumentType documentType;
    private String documentName;

    private String originalFileName;
    private String storagePath;
    private String contentType;
    private Long fileSize;

    private String documentNumber;
    private LocalDateTime issuedAt;
    private String issuer;
    private LocalDateTime expiresAt;
    private DocumentStatus documentStatus;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;
    private UUID verifiedBy;
    private LocalDateTime verifiedAt;

    private UUID rejectedBy;
    private LocalDateTime rejectedAt;
    private String rejectionReason;
}
