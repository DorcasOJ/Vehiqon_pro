package com.vehiqon.features.wallet.controller;

import com.vehiqon.common.dto.response.ApiResponse;
import com.vehiqon.common.mapper.ApiResponseMapper;
import com.vehiqon.features.wallet.service.VirtualAccountService;
import com.vehiqon.integrations.nomba.dto.NombaDto;
import com.vehiqon.integrations.nomba.service.NombaTokenService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/wallet")
@AllArgsConstructor
@RestController
@SecurityRequirement(name = "bearerAuth")
public class WalletController {
    private final NombaTokenService nombaTokenService;
    private final VirtualAccountService virtualAccountService;
    private final ApiResponseMapper apiResponseMapper;
//    private final AuthService authService;

    @GetMapping("/nomba-token")
    public String token() {
        return nombaTokenService.getValidAccessToken();
    }

    @PostMapping("/virtual-account")
    public ApiResponse<NombaDto.VirtualAccountResponse.Data> create() {

        return apiResponseMapper.toResponse(
               virtualAccountService.createVirtualAccount()
        );

    }

    @GetMapping("/virtual-account")
    public ApiResponse<NombaDto.VirtualAccountResponse.Data> getVirtualAccount() {
        return apiResponseMapper.toResponse(
                virtualAccountService.getPrimaryAccount()
        );

    }
}
