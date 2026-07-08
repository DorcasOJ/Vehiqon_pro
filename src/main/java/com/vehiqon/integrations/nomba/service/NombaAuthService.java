package com.vehiqon.integrations.nomba.service;

import com.vehiqon.integrations.nomba.dto.NombaDto;

public interface NombaAuthService {
    NombaDto.NombaTokenResponse issueToken();
    NombaDto.NombaTokenResponse refreshToken(String refreshToken);
//    NombaDto.VirtualAccountResponse createVirtualAccount(UserEntity user);
}
