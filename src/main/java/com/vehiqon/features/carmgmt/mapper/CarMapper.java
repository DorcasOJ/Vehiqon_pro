package com.vehiqon.features.carmgmt.mapper;


import com.vehiqon.common.dto.mapper.DateMapper;
import com.vehiqon.features.carmgmt.dto.CarDocumentDto;
import com.vehiqon.features.carmgmt.dto.CarDto;
import com.vehiqon.features.carmgmt.entities.CarDocumentEntity;
import com.vehiqon.features.carmgmt.entities.CarEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {DateMapper.class}
)
public interface CarMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
//    @Mapping(target = "documentStatus", ignore = true)
    @Mapping(source = "purchaseDate", target = "purchaseDate")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(source = "licenseExpiry", target = "licenseExpiry")
    CarEntity toEntity(CarDto.CreateCarRequest car);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
//    @Mapping(target = "documentStatus", ignore = true)
    @Mapping(source = "purchaseDate", target = "purchaseDate")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(source = "licenseExpiry", target = "licenseExpiry")
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(CarDto.UpdateCarRequest request,
                      @MappingTarget CarEntity entity);


    CarDto.CarEntityResponse toResponse(CarEntity car);

    List<CarDto.CarEntityResponse> toListResponse(List<CarEntity> cars);

    @Mapping(target = "downloadUrl", ignore = true)
    @Mapping(target = "withDownloadUrl", ignore = true)
    CarDocumentDto.CarDocumentResponse toCarDocResponse(CarDocumentEntity request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "carId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "originalFileName", ignore = true)
    @Mapping(target = "storagePath", ignore = true)
    @Mapping(target = "contentType", ignore = true)
    @Mapping(target = "fileSize", ignore = true)
    @Mapping(target = "verificationStatus", ignore = true)
    @Mapping(target = "rejectedAt", ignore = true)
    @Mapping(target = "rejectedBy", ignore = true)
    @Mapping(target = "rejectionReason", ignore = true)
    @Mapping(target = "verifiedBy", ignore = true)
    @Mapping(target = "verifiedAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    CarDocumentEntity toCarDocEntity (CarDocumentDto.UploadCarDoc request);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "carId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "originalFileName", ignore = true)
    @Mapping(target = "storagePath", ignore = true)
    @Mapping(target = "contentType", ignore = true)
    @Mapping(target = "fileSize", ignore = true)
    @Mapping(target = "verificationStatus", ignore = true)
    @Mapping(target = "rejectedAt", ignore = true)
    @Mapping(target = "rejectedBy", ignore = true)
    @Mapping(target = "rejectionReason", ignore = true)
    @Mapping(target = "verifiedBy", ignore = true)
    @Mapping(target = "verifiedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateCarDocEntity (CarDocumentDto.UpdateCarDoc request,
                             @MappingTarget CarDocumentEntity entity);

}



