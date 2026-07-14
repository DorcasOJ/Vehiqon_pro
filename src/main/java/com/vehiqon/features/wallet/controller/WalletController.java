package com.vehiqon.features.wallet.controller;

import com.vehiqon.common.dto.response.ApiResponse;
import com.vehiqon.common.mapper.ApiResponseMapper;
import com.vehiqon.features.wallet.service.VirtualAccountService;
import com.vehiqon.integrations.nomba.dto.NombaDto;
import com.vehiqon.integrations.nomba.service.NombaTokenService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/wallet")
@AllArgsConstructor
@RestController
@SecurityRequirement(name = "bearerAuth")
public class WalletController {
    private final NombaTokenService nombaTokenService;
    private final VirtualAccountService virtualAccountService;
    private final ApiResponseMapper apiResponseMapper;

//    @GetMapping("/nomba-token")
//    public String token() {
//        return nombaTokenService.getValidAccessToken();
//    }

    @PostMapping("/virtual-account")
    public ApiResponse<NombaDto.VirtualAccountResponse.Data> create() {
        return apiResponseMapper.toResponse(
               virtualAccountService.createVirtualAccount()
        );
    }

    @GetMapping("/virtual-account")
    public ApiResponse<NombaDto.VirtualAccountResponse.Data> getVirtualAccount() {
        return apiResponseMapper.toResponse(
                virtualAccountService.getVirtualAccount()
        );

    }

    @PutMapping("/virtual-account")
    public ApiResponse<NombaDto.VirtualAccountResponse.Data> updateVirtualAccount(
           @Valid @RequestBody NombaDto.UpdateVirtualAccountName request
    ) {
        return apiResponseMapper.toResponse(
                virtualAccountService.updateVirtualAccountName(request)
        );

    }
}
