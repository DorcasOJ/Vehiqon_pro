package com.vehiqon.integrations.nomba.client;

import com.vehiqon.integrations.nomba.config.NombaProperties;
import com.vehiqon.integrations.nomba.dto.NombaDto;
import com.vehiqon.integrations.nomba.service.NombaTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class NombaClient {
    private final RestClient restClient;
    private final NombaTokenService nombaTokenService;
    private final NombaProperties properties;

    public NombaDto.VirtualAccountResponse createVirtualAccount(
            NombaDto.CreateVirtualAccountRequest request) {

        return restClient.post()
                .uri(properties.getBaseUrl() + "/v1/accounts/virtual")
                .header(HttpHeaders.AUTHORIZATION,
                        "Bearer " + nombaTokenService.getValidAccessToken())
                .header("accountId", properties.getAccountId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(NombaDto.VirtualAccountResponse.class);
    }

    public NombaDto.UpdateVirtualAccountResponse updateVirtualAccountName(
            NombaDto.UpdateVirtualAccountName request, String accountNumber ) {

        return restClient.put()
                .uri(properties.getBaseUrl() + "/v1/accounts/virtual/"+ accountNumber )
                .header(HttpHeaders.AUTHORIZATION,
                        "Bearer " + nombaTokenService.getValidAccessToken())
                .header("accountId", properties.getAccountId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(NombaDto.UpdateVirtualAccountResponse.class);
    }
}
