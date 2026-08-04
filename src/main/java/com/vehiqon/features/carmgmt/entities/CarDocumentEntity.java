package com.vehiqon.features.carmgmt.entities;


import com.vehiqon.common.entity.BaseWithDeleteEntity;
import com.vehiqon.features.carmgmt.enums.DocumentStatus;
import com.vehiqon.features.carmgmt.enums.DocumentType;
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
public class CarDocuments extends BaseWithDeleteEntity {

    private UUID carId;

    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    private String originalFileName;
    private String storagePath;
    private String mimeType;
    private Integer size;

    private String documentNumber;
    private LocalDateTime issuedAt;
    private String issuer;
    private LocalDateTime expiresAt;
    private DocumentStatus status;

    @Builder.Default
    @Column(nullable = false)
    private boolean verified = false;

    private UUID verifiedBy;
    private LocalDateTime verifiedAt;
}
