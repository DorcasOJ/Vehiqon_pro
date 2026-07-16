package com.vehiqon.features.wallet.mapper;

import com.vehiqon.integrations.nomba.dto.NombaDto;
import com.vehiqon.features.wallet.entity.VirtualAccountEntity;
import com.vehiqon.integrations.nomba.entity.NombaTokenEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NombaMapper {

    @Mapping(target = "accountRef", source = "accountReference")
//    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "currency", constant = "NGN")
    @Mapping(source = "accountNumber", target = "bankAccountNumber")
    @Mapping(source = "accountName", target = "bankAccountName")
    @Mapping(source = "bankName", target = "bankName")
    @Mapping(source = "accountHolderId", target = "accountHolderId")
    NombaDto.VirtualAccountResponse.Data toVirtualAccResponse(VirtualAccountEntity virtualAccountEntity);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "accountReference", source = "accountRef")
    @Mapping(target = "accountNumber", source = "bankAccountNumber")
    @Mapping(target = "accountName", source = "bankAccountName")
    @Mapping(target = "bankName", source = "bankName")
    @Mapping(target = "accountHolderId", source = "accountHolderId")
    @Mapping(target = "expired", constant = "false")
    VirtualAccountEntity toVirtualAccEntity(NombaDto.VirtualAccountResponse.Data response);

//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "updatedAt", ignore = true)
//    @Mapping(target = "createdAt", ignore = true)
//    NombaTokenEntity toTokenEntity(NombaDto.NombaTokenResponse.TokenResponseData  response);

}
