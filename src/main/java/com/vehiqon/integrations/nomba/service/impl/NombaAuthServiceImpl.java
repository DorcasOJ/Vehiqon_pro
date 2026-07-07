package com.vehiqon.integrations.nomba.service.impl;

import com.vehiqon.integrations.nomba.config.NombaProperties;
import com.vehiqon.integrations.nomba.service.NombaAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class NombaAuthServiceImpl implements NombaAuthService {

    private final RestClient restClient;
    private final NombaProperties properties;

    @Override
    public String getAccessToken() {

        // Call OAuth endpoint

        // Cache token until expiry
        return "";
    }
}
