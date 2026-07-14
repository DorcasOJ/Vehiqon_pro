package com.vehiqon.common.entity;


import com.vehiqon.features.carmgmt.entities.CarEntity;
import com.vehiqon.features.carmgmt.enums.DocumentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name="car_documents")
public class Documents extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    private CarEntity carEntity;

    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    private String fileUrl;

    private LocalDate expiryDate;}
