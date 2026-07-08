package com.vehiqon.integrations.nomba.service.impl;

import com.vehiqon.common.exception.BadRequestException;
import com.vehiqon.features.onboarding.entity.UserEntity;
import com.vehiqon.integrations.nomba.client.NombaClient;
import com.vehiqon.integrations.nomba.config.NombaProperties;
import com.vehiqon.integrations.nomba.dto.NombaDto;
import com.vehiqon.features.wallet.entity.VirtualAccountEntity;
import com.vehiqon.features.wallet.mapper.NombaMapper;
import com.vehiqon.features.wallet.repository.VirtualAccountRepository;
import com.vehiqon.integrations.nomba.service.NombaAuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NombaAuthServiceImpl implements NombaAuthService {

    private final RestClient restClient;
    private final NombaProperties properties;


    @Override
    public NombaDto.NombaTokenResponse issueToken() {
        Map<String, String> body = Map.of(
                "grant_type", "client_credentials",
                "client_id", properties.getClientId(),
                "client_secret", properties.getClientSecret()
        );

        NombaDto.NombaTokenResponse response = restClient.post()
                .uri(properties.getBaseUrl() + "/v1/auth/token/issue")
                .header("accountId", properties.getAccountId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(NombaDto.NombaTokenResponse.class);

        if(response == null || response.data() == null)
            throw new BadRequestException("Unable to obtain Nomba access token");
        return response;

    }

    @Override
    public NombaDto.NombaTokenResponse refreshToken(String refreshToken) {
        Map<String, String> body = Map.of(
                "grant_type", "refresh_token",
                "refresh_token", refreshToken
        );

        return restClient.post()
                .uri(properties.getBaseUrl() + "/v1/auth/token/refresh")
                .header("accountId", properties.getAccountId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(NombaDto.NombaTokenResponse.class);
    }


}
