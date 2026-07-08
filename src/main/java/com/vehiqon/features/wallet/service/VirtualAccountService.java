package com.vehiqon.features.wallet.service;

import com.vehiqon.integrations.nomba.dto.NombaDto;

public interface VirtualAccountService {
    NombaDto.VirtualAccountResponse.Data createVirtualAccount();
    NombaDto.VirtualAccountResponse.Data getVirtualAccount();
    NombaDto.VirtualAccountResponse.Data updateVirtualAccountName(NombaDto.UpdateVirtualAccountName accountName);

}
